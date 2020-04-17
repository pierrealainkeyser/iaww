package fr.keyser.wonderfull.world.game;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.security.UserPrincipal;

public class InGameId {

	private final String external;

	private final UserPrincipal user;

	private final InstanceId game;

	private final int index;

	public InGameId(String external, InstanceId game, int index, UserPrincipal user) {
		this.external = external;
		this.game = game;
		this.index = index;
		this.user = user;
	}

	public String getExternal() {
		return external;
	}

	public InstanceId getGame() {
		return game;
	}

	public int getIndex() {
		return index;
	}

	public UserPrincipal getUser() {
		return user;
	}

	@Override
	public String toString() {
		return String.format("{user=%s, external=%s}", user, external);
	}

}
