package fr.keyser.wonderfull.world.game;

import java.util.List;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.world.ActiveGameDescription;
import fr.keyser.wonderfull.world.PlayerGameDescription;

public interface ActiveGameRepository {

	public void register(ActiveGame game);

	public void unregister(ActiveGame game);

	public ActiveGame findById(InstanceId id);

	public ResolvedGame findByExternal(String externalId);

	public void update(ResolvedGame game);

	public List<PlayerGameDescription> gamesFor(String user);

	public List<ActiveGameDescription> games();
}
