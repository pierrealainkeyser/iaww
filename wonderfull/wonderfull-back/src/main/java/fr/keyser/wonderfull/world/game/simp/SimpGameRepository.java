package fr.keyser.wonderfull.world.game.simp;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.world.ActiveGameDescription;
import fr.keyser.wonderfull.world.PlayerGameDescription;
import fr.keyser.wonderfull.world.game.ActiveGame;
import fr.keyser.wonderfull.world.game.ActiveGameRepository;
import fr.keyser.wonderfull.world.game.InGameId;
import fr.keyser.wonderfull.world.game.ResolvedGame;

public class SimpGameRepository implements ActiveGameRepository {

	private final ActiveGameRepository delegated;

	private final SimpMessageSendingOperations simp;

	public SimpGameRepository(ActiveGameRepository delegated, SimpMessageSendingOperations simp) {
		this.delegated = delegated;
		this.simp = simp;
	}

	@Override
	public ResolvedGame findByExternal(String externalId) {
		return delegated.findByExternal(externalId);
	}

	@Override
	public ActiveGame findById(InstanceId id) {
		return delegated.findById(id);
	}

	@Override
	public List<ActiveGameDescription> games() {
		return delegated.games();
	}

	@Override
	public List<PlayerGameDescription> gamesFor(String user) {
		return delegated.gamesFor(user);
	}

	@Override
	public void register(ActiveGame game) {
		delegated.register(game);
		broadcast(game);
	}

	private void broadcast(ActiveGame game) {
		for (InGameId igi : game.getPlayers()) {
			String user = igi.getUser().getName();
			List<PlayerGameDescription> games = gamesFor(user);
			simp.convertAndSendToUser(user, "/my-games", games);
		}
	}

	@Override
	public void unregister(ActiveGame game) {
		delegated.unregister(game);
		broadcast(game);
	}

	@Override
	public void update(ResolvedGame game) {
		delegated.update(game);
	}

}
