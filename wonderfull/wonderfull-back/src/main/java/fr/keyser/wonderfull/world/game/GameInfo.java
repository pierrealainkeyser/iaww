package fr.keyser.wonderfull.world.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.action.ActionDispatch;
import fr.keyser.wonderfull.world.event.EventAt;

public class GameInfo {

	private final Game game;

	private final int turn;

	@JsonProperty
	private final int drafteds;

	private final int clock;

	private final List<EventAt> events;

	private final List<EventAt> privateEvents;

	public GameInfo(Game game) {
		this(game, 0, 0, 0, Collections.emptyList(), Collections.emptyList());
	}

	@JsonCreator
	public GameInfo(@JsonProperty("game") Game game, @JsonProperty("turn") int turn,
			@JsonProperty("drafteds") int drafteds, @JsonProperty("clock") int clock,
			@JsonProperty("events") List<EventAt> events, @JsonProperty("privateEvents") List<EventAt> privateEvents) {
		this.game = game;
		this.turn = turn;
		this.drafteds = drafteds;
		this.clock = clock;
		this.events = Collections.unmodifiableList(events != null ? events : Collections.emptyList());
		this.privateEvents = Collections
				.unmodifiableList(privateEvents != null ? privateEvents : Collections.emptyList());
	}

	public GameInfo reloadDictionnary(MetaCardDictionnaryLoader loader) {
		return new GameInfo(game.reloadDictionnary(loader), turn, drafteds, clock, events, privateEvents);
	}

	public int getClock() {
		return clock;
	}

	public List<EventAt> getEvents() {
		return events;
	}

	public Game getGame() {
		return game;
	}

	public int getTurn() {
		return turn;
	}

	@JsonIgnore
	public boolean isDoneDrafting() {
		return drafteds >= 7;
	}

	@JsonIgnore
	public boolean isDonePlaying() {
		return turn >= 4;
	}

	public Integer winner() {
		return game.winner();
	}

	public List<PlayerScoreBoard> scoreBoards() {
		return game.scoreBoards();
	}

	public GameInfo nextDraft() {
		return new GameInfo(game.nextDraftStep(turn), turn, drafteds + 1, clock, events, privateEvents).publishEvents();
	}

	public GameInfo publishEvents() {
		List<EventAt> newEvents = new ArrayList<>(events.size() + privateEvents.size());
		newEvents.addAll(events);
		newEvents.addAll(privateEvents);

		return new GameInfo(game, turn, drafteds, clock, newEvents, Collections.emptyList());
	}

	public GameInfo nextTurn() {
		return new GameInfo(game.nextTurn(), turn + 1, 0, clock, events, privateEvents);
	}

	public GameInfo nextTurnSinglePlayer() {
		return new GameInfo(game, turn + 1, 0, clock, events, privateEvents);
	}

	public boolean singlePlayerDraftedEmpty() {
		return game.getEmpires().get(0).getPlanning().getDrafteds().isEmpty();
	}

	@Override
	public String toString() {
		return String.format("[turn=%s, drafteds=%s, clock=%s]]", turn, drafteds, clock);
	}

	public GameInfo dispatch(ActionDispatch dispatch) {
		int nextClock = clock + 1;
		List<EventAt> newEvents = new ArrayList<>(events);
		Game newGame = dispatchAction(dispatch, nextClock, newEvents);
		return new GameInfo(newGame, turn, drafteds, nextClock, newEvents, privateEvents);
	}

	public GameInfo privateDispatch(ActionDispatch dispatch) {
		int nextClock = clock + 1;
		List<EventAt> newEvents = new ArrayList<>(privateEvents);
		Game newGame = dispatchAction(dispatch, nextClock, newEvents);
		return new GameInfo(newGame, turn, drafteds, nextClock, events, newEvents);
	}

	private Game dispatchAction(ActionDispatch dispatch, int nextClock, List<EventAt> newEvents) {
		return game.dispatch(dispatch, (player, event) -> newEvents.add(new EventAt(nextClock, player, event)));
	}

	public GameInfo startProductionStep(Token step) {
		return new GameInfo(game.startProductionStep(step), turn, drafteds, clock + 1, events, privateEvents);
	}

	public GameInfo startPlanning() {
		return new GameInfo(game.startPlanning(), turn, drafteds, clock + 1, events, privateEvents);
	}

	public GameInfo startSinglePlayerPlanning() {
		return new GameInfo(game.startSinglePlayerPlanning(), turn, drafteds, clock + 1, events, privateEvents);
	}

	public GameInfo endProductionStep() {
		int nextClock = clock + 1;
		List<EventAt> newEvents = new ArrayList<>(events);
		Game newGame = game.endProductionStep((player, event) -> newEvents.add(new EventAt(nextClock, player, event)));
		return new GameInfo(newGame, turn, drafteds, nextClock, newEvents, privateEvents);
	}
}
