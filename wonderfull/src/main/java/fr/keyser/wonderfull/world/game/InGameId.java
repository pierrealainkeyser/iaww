package fr.keyser.wonderfull.world.game;

import fr.keyser.fsm.InstanceId;

public class InGameId {

	private final String external;

	private final String user;

	private final InstanceId game;

	private final int index;

	public InGameId(String external, InstanceId game, int index, String user) {
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

	public String getUser() {
		return user;
	}

	@Override
	public String toString() {
		return String.format("{user=%s, external=%s}", user, external);
	}

}
