package fr.keyser.wonderfull.world.game;

import java.util.OptionalInt;

import fr.keyser.fsm.Choice;
import fr.keyser.fsm.Edge;
import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.Node;
import fr.keyser.fsm.Region;
import fr.keyser.fsm.Transition;
import fr.keyser.fsm.TransitionNode;
import fr.keyser.fsm.impl.Automats;
import fr.keyser.fsm.impl.AutomatsBuilder;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.action.ActionDispatch;
import fr.keyser.wonderfull.world.action.ActionSupremacy;

public class GameAutomatsBuilder {

	public static final String END_STATE = "ended";

	public static final String PRODUCTION_STATE = "production";

	public static final String PLANNING_STATE = "planning";

	public static final String DRAFT_STATE = "draft";

	private static final String PLAYERS_STATE = "players";

	public static final String WAITING_STATE = "waiting";

	public static final String SUPREMACY_STATE = "supremacy";

	public static final String PASS_EVENT = "pass";

	public static final String PLAY_EVENT = "play";

	public Automats<GameInfo> build(InstanceId id, int nbPlayers, boolean krystaliumStep) {

		AutomatsBuilder<GameInfo> builder = new AutomatsBuilder<>();

		TransitionNode<GameInfo> initial = builder.auto("initial");

		TransitionNode<GameInfo> nextTurn = builder.auto("nextTurn");
		boolean multiplayer = nbPlayers > 1;
		if (multiplayer)
			nextTurn.updateEntry(this::nextTurn);
		else
			nextTurn.updateEntry(this::nextTurnSinglePlayer);

		Node<GameInfo> draft = multiplayer ? builder.node(DRAFT_STATE) : null;
		Node<GameInfo> planning = builder.node(PLANNING_STATE);
		Node<GameInfo> production = builder.node(PRODUCTION_STATE);

		Choice<GameInfo> eog = builder.choice("checkEOG");
		Node<GameInfo> end = builder.node(END_STATE);

		eog.when(this::stillPlaying, nextTurn).otherwise(end);

		if (multiplayer)
			draft(draft, nbPlayers, planning);
		planning(planning, nbPlayers, production);
		production(production, nbPlayers, krystaliumStep, eog);

		initial.to(nextTurn);
		if (multiplayer)
			nextTurn.to(draft);
		else
			nextTurn.to(planning);

		return builder.build(id);
	}

	private boolean stillPlaying(Instance<GameInfo> instance, Transition transition) {
		return !instance.get(GameInfo::isDonePlaying);
	}

	private void draft(Node<GameInfo> draft, int nbPlayers, Edge planning) {
		Region<GameInfo> players = draft.region(PLAYERS_STATE, nbPlayers).merge(this::privateDispatch);
		TransitionNode<GameInfo> clean = draft.auto("clean");
		TransitionNode<GameInfo> nextDraftStep = draft.auto("nextDraftStep");
		Choice<GameInfo> shallDraft = draft.choice("shallDraft");

		Node<GameInfo> waiting = players.node(WAITING_STATE);
		Edge joinPoint = players.joinTo(nextDraftStep);
		waiting.event(PLAY_EVENT, joinPoint).guard(this::acceptDispatchAction);
		waiting.callbackExit(this::mergePayload);

		nextDraftStep.updateEntry(this::nextDraftStep);
		nextDraftStep.to(shallDraft);

		clean.updateEntry(this::publishEvents);
		clean.to(planning);

		shallDraft.when(this::isDoneDrafting, clean).otherwise(players);
	}

	private void planning(Node<GameInfo> planning, int nbPlayers, Edge production) {

		boolean multiplayer = nbPlayers > 1;

		if (multiplayer) {
			TransitionNode<GameInfo> init = createPlanning(planning, nbPlayers, false, production);
			init.updateEntry(this::startPlanning);
		} else {

			Node<GameInfo> first = planning.node("first");
			Node<GameInfo> second = planning.node("second");

			TransitionNode<GameInfo> firstI = createPlanning(first, nbPlayers, true, second);
			firstI.updateEntry(this::startSinglePlayerPlanning);

			TransitionNode<GameInfo> secondI = createPlanning(second, nbPlayers, false, production);
			secondI.updateEntry(this::startSinglePlayerPlanning);
		}

	}

	private TransitionNode<GameInfo> createPlanning(Node<GameInfo> planning, int nbPlayers, boolean auto,
			Edge production) {
		TransitionNode<GameInfo> init = planning.auto("init");
		Region<GameInfo> players = playersState(planning, nbPlayers);
		init.to(players);
		if (auto)
			singlePlayerPlanning(players, production);
		else
			playableRegion(players, production);
		return init;
	}

	private Region<GameInfo> playersState(Node<GameInfo> node, int nbPlayers) {
		return node.region(PLAYERS_STATE, nbPlayers).merge(this::dispatch);
	}

	private void production(Node<GameInfo> production, int nbPlayers, boolean krystaliumStep, Edge next) {
		Node<GameInfo> material = production.node(Token.MATERIAL.name());
		Node<GameInfo> energy = production.node(Token.ENERGY.name());
		Node<GameInfo> science = production.node(Token.SCIENCE.name());
		Node<GameInfo> gold = production.node(Token.GOLD.name());
		Node<GameInfo> discovery = production.node(Token.DISCOVERY.name());

		Node<GameInfo> krystalium = null;
		if (krystaliumStep)
			krystalium = production.node(Token.KRYSTALIUM.name());

		productionStep(material, Token.MATERIAL, nbPlayers, energy);
		productionStep(energy, Token.ENERGY, nbPlayers, science);
		scienceProductionStep(science, Token.SCIENCE, nbPlayers, gold);
		productionStep(gold, Token.GOLD, nbPlayers, discovery);

		if (krystalium == null)
			productionStep(discovery, Token.DISCOVERY, nbPlayers, next);
		else {
			productionStep(discovery, Token.DISCOVERY, nbPlayers, krystalium);
			productionStep(krystalium, Token.KRYSTALIUM, nbPlayers, next);
		}
	}

	private GameInfo privateDispatch(GameInfo container, Object payload) {
		if (payload instanceof ActionDispatch) {
			return container.privateDispatch((ActionDispatch) payload);
		}
		return container;
	}

	private GameInfo dispatch(GameInfo container, Object payload) {
		if (payload instanceof ActionDispatch) {
			return container.dispatch((ActionDispatch) payload);
		}
		return container;
	}

	private void productionStep(Node<GameInfo> production, Token step, int nbPlayers, Edge next) {
		TransitionNode<GameInfo> init = production.auto("init");
		init.updateEntry((container, evt) -> {
			GameInfo started = container.startProductionStep(step);
			OptionalInt supremacy = started.getGame().supremacyIndex(step);
			if (supremacy.isPresent()) {
				ActionDispatch action = new ActionSupremacy(true).dispatch(supremacy.getAsInt());
				started = started.dispatch(action);
			}
			return started;
		});

		Region<GameInfo> players = playersState(production, nbPlayers);
		init.to(players);

		TransitionNode<GameInfo> end = production.auto("end");
		end.updateEntry((container, evt) -> container.endProductionStep());

		playableRegion(players, end);

		end.to(next);
	}

	private void scienceProductionStep(Node<GameInfo> production, Token step, int nbPlayers, Edge next) {
		Node<GameInfo> init = production.node("init");
		Region<GameInfo> supremacy = production.region("supremacy", nbPlayers).merge(this::dispatch);
		Region<GameInfo> players = playersState(production, nbPlayers);

		init.updateEntry((container, evt) -> container.startProductionStep(step));
		init.callbackEntry((i, evt) -> {
			OptionalInt index = i.get(GameInfo::getGame).supremacyIndex(step);
			if (index.isPresent()) {
				// activate supremacy
				i.unicast("toSupremacy", index.getAsInt());
			} else {
				i.unicast("skip");
			}
		});
		init.event("skip", players);
		init.event("toSupremacy", supremacy);

		Node<GameInfo> player = supremacy.node("player");
		Edge supremacyJointPoint = supremacy.joinTo(players);
		Node<GameInfo> decide = player.node("decide");
		Node<GameInfo> waitingSup = player.node(SUPREMACY_STATE);
		decide.callbackEntry((i, evt) -> {
			// only one player will choose the supremacy token
			int index = i.getIndex();
			if (Integer.valueOf(index).equals(evt.getPayload()))
				i.unicast("proceed");
			else
				i.unicast("skip");
		});
		decide.event("proceed", waitingSup);
		decide.event("skip", supremacyJointPoint);

		waitingSup.event(PLAY_EVENT, supremacyJointPoint);
		waitingSup.callbackExit(this::mergePayload);

		TransitionNode<GameInfo> end = production.auto("end");
		end.updateEntry((container, evt) -> container.endProductionStep());

		playableRegion(players, end);

		end.to(next);
	}

	private void playableRegion(Region<GameInfo> region, Edge next) {
		Node<GameInfo> input = region.node("input");
		Node<GameInfo> waiting = input.node(WAITING_STATE);
		Edge joinPoint = region.joinTo(next);

		waiting.event(PASS_EVENT, joinPoint).guard(this::acceptDispatchAction);
		waiting.event(PLAY_EVENT, waiting).guard(this::acceptDispatchAction);
		waiting.callbackExit(this::mergePayload);
	}

	private void singlePlayerPlanning(Region<GameInfo> region, Edge next) {
		Node<GameInfo> input = region.node("input");
		Node<GameInfo> waiting = input.node(WAITING_STATE);
		Choice<GameInfo> choice = input.choice("check");

		Edge joinPoint = region.joinTo(next);

		waiting.event(PLAY_EVENT, choice).guard(this::acceptDispatchAction);
		waiting.callbackExit(this::mergePayload);

		choice.when(this::hasDoneSinglePlayerPlanning, joinPoint).otherwise(waiting);
	}

	private boolean hasDoneSinglePlayerPlanning(Instance<GameInfo> i, Transition t) {
		return i.getParent().get(GameInfo::singlePlayerDraftedEmpty);
	}

	private GameInfo startPlanning(GameInfo container, EventMsg msg) {
		return container.startPlanning();
	}

	private GameInfo startSinglePlayerPlanning(GameInfo container, EventMsg msg) {
		return container.startSinglePlayerPlanning();
	}

	private boolean isDoneDrafting(Instance<GameInfo> instance, Transition transition) {
		return instance.get(GameInfo::isDoneDrafting);
	}

	private void mergePayload(Instance<GameInfo> i, EventMsg msg) {
		i.sendMerge(msg.getPayload());
	}

	/**
	 * Only accecept dispatch action
	 * 
	 * @param t
	 * @param transition
	 * @return
	 */
	private boolean acceptDispatchAction(Instance<GameInfo> t, Transition transition) {
		Object payload = transition.getEvent().getPayload();
		if (payload instanceof ActionDispatch) {
			ActionDispatch dispatch = (ActionDispatch) payload;
			int index = t.getIndex();
			return dispatch.getIndex() == index;
		}

		return false;
	}

	private GameInfo nextTurn(GameInfo game, EventMsg msg) {
		return game.nextTurn();
	}

	private GameInfo nextTurnSinglePlayer(GameInfo game, EventMsg msg) {
		return game.nextTurnSinglePlayer();
	}

	private GameInfo nextDraftStep(GameInfo container, EventMsg msg) {
		return container.nextDraft();
	}

	private GameInfo publishEvents(GameInfo container, EventMsg msg) {
		return container.publishEvents();
	}

}
