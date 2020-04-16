package fr.keyser.wonderfull.world.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.keyser.fsm.Instance;
import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.impl.Automats;
import fr.keyser.fsm.impl.PoisonPill;
import fr.keyser.wonderfull.world.ActiveGameDescription;
import fr.keyser.wonderfull.world.EmpireConfiguration;
import fr.keyser.wonderfull.world.GameConfiguration;
import fr.keyser.wonderfull.world.PlayerGameDescription;

public class ActiveGame {

	private final List<InGameId> players;

	private final Automats<GameInfo> automats;

	public ActiveGame(Automats<GameInfo> automats) {
		InstanceId id = automats.getId();
		Instance<GameInfo> first = automats.instances().get(0);
		GameConfiguration conf = first.get(GameInfo::getGame).getConfiguration();

		List<InGameId> players = new ArrayList<>();
		for (int i = 0; i < conf.getEmpires().size(); ++i) {
			EmpireConfiguration e = conf.getEmpires().get(i);
			players.add(new InGameId(e.getExternalId(), id, i, e.getUser()));
		}

		this.players = Collections.unmodifiableList(players);
		this.automats = automats;
	}

	GameConfiguration configuration() {
		Instance<GameInfo> first = automats.instances().get(0);
		return first.get(GameInfo::getGame).getConfiguration();
	}

	public boolean isTerminated() {
		Instance<GameInfo> first = automats.instances().get(0);
		List<String> state = first.getState().values();
		return GameAutomatsBuilder.END_STATE.equals(state.get(0));

	}

	public boolean contains(String user) {
		return players.stream().anyMatch(p -> p.getUser().equals(user));
	}

	public List<InGameId> getPlayers() {
		return players;
	}

	public void kill() {
		automats.submit(PoisonPill.ALL);
	}

	public Automats<GameInfo> getAutomats() {
		return automats;
	}

	public InstanceId getId() {
		return automats.getId();
	}

	public PlayerGameDescription asDescription(String user) {
		return configuration().asDescription(user, isTerminated());
	}

	public ActiveGameDescription asDescription() {
		return configuration().asGameDescription(automats.getId());
	}

}
