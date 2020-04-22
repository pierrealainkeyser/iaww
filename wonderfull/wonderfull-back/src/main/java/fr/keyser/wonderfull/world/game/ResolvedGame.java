package fr.keyser.wonderfull.world.game;

import java.util.List;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.impl.Automats;
import fr.keyser.wonderfull.world.action.ActionPass;
import fr.keyser.wonderfull.world.action.EmpirePlayAction;

public class ResolvedGame {

	private final int myself;

	private final ActiveGame game;

	public ResolvedGame(int myself, ActiveGame game) {
		this.myself = myself;
		this.game = game;
	}

	public Instance<GameInfo> play(EmpirePlayAction action) {
		Automats<GameInfo> automats = getAutomats();
		automats.submit(EventMsg.broadcast(GameAutomatsBuilder.PLAY_EVENT, action.dispatch(myself)));
		return automats.instances().get(0);
	}

	public ActiveGame getGame() {
		return game;
	}

	private Automats<GameInfo> getAutomats() {
		return game.getAutomats();
	}

	public Instance<GameInfo> instance() {
		return getAutomats().instances().get(0);
	}

	public Instance<GameInfo> pass() {
		Automats<GameInfo> automats = getAutomats();
		automats.submit(EventMsg.broadcast(GameAutomatsBuilder.PASS_EVENT, ActionPass.PASS.dispatch(myself)));
		return automats.instances().get(0);
	}

	public InstanceId getGameId() {
		return getAutomats().getId();
	}

	public List<InGameId> getPlayers() {
		return game.getPlayers();
	}

	public int getMyself() {
		return myself;
	}

}
