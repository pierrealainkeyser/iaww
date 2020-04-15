package fr.keyser.wonderfull.world.game;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.world.ActiveGameDescription;
import fr.keyser.wonderfull.world.PlayerGameDescription;
import fr.keyser.wonderfull.world.UnresolvedGameIdException;

public class ActiveGameMapRepository implements ActiveGameRepository {

	private final Map<InstanceId, ActiveGame> games;

	private final Map<String, InGameId> players;

	public ActiveGameMapRepository() {
		this(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
	}

	public ActiveGameMapRepository(Map<InstanceId, ActiveGame> games, Map<String, InGameId> players) {
		this.games = games;
		this.players = players;
	}

	@Override
	public List<ActiveGameDescription> games() {
		return games.values().stream().map(ActiveGame::asDescription).collect(Collectors.toList());
	}

	@Override
	public List<PlayerGameDescription> gamesFor(String user) {
		return games.values().stream().filter(a -> a.contains(user)).map(a -> a.asDescription(user))
				.collect(Collectors.toList());
	}

	@Override
	public void register(ActiveGame game) {
		games.compute(game.getId(), (id, old) -> {
			for (InGameId player : game.getPlayers()) {
				players.put(player.getExternal(), player);
			}
			return game;
		});
	}

	@Override
	public ActiveGame findById(InstanceId id) {
		return games.get(id);
	}

	@Override
	public void unregister(ActiveGame game) {
		games.compute(game.getId(), (id, old) -> {
			if (old != null) {
				for (InGameId player : old.getPlayers()) {
					players.remove(player.getExternal());
				}
			}
			return null;
		});
	}

	@Override
	public ResolvedGame findByExternal(String externalId) {
		InGameId player = players.get(externalId);
		if (player == null)
			throw new UnresolvedGameIdException(externalId);

		ActiveGame activeGame = games.get(player.getGame());
		if (activeGame == null)
			throw new UnresolvedGameIdException(externalId);

		return new ResolvedGame(player.getIndex(), activeGame);
	}

	@Override
	public void update(ResolvedGame resolved) {
		ActiveGame game = resolved.getGame();
		games.put(game.getId(), game);

	}

}
