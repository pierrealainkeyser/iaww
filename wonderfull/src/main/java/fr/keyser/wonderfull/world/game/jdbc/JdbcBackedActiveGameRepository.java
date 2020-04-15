package fr.keyser.wonderfull.world.game.jdbc;

import java.util.List;

import javax.annotation.PostConstruct;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.world.ActiveGameDescription;
import fr.keyser.wonderfull.world.PlayerGameDescription;
import fr.keyser.wonderfull.world.game.ActiveGame;
import fr.keyser.wonderfull.world.game.ActiveGameRepository;
import fr.keyser.wonderfull.world.game.ResolvedGame;

public class JdbcBackedActiveGameRepository implements ActiveGameRepository {

	private final ActiveGameRepository delegated;

	private final JdbcGameRepository storage;

	public JdbcBackedActiveGameRepository(ActiveGameRepository delegated, JdbcGameRepository storage) {
		this.delegated = delegated;
		this.storage = storage;
	}

	@PostConstruct
	public void reload() {
		for (ActiveGame game : storage.reload()) {
			delegated.register(game);
		}
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
		storage.create(game);
		delegated.register(game);

	}

	@Override
	public void unregister(ActiveGame game) {
		storage.delete(game);
		delegated.unregister(game);

	}

	@Override
	public void update(ResolvedGame game) {
		storage.update(game.getGame());
		delegated.update(game);
	}

}
