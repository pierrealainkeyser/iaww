package fr.keyser.wonderfull.world;

import java.time.Instant;
import java.util.List;

import fr.keyser.wonderfull.security.UserPrincipal;

public class PlayerGameDescription extends AbstractGameDescription {

	private final String externalId;

	private final boolean terminated;

	public PlayerGameDescription(String externalId, boolean terminated,String creator, List<String> dictionaries, String startingEmpire, List<UserPrincipal> users,
			Instant createdAt) {
		super(creator,dictionaries, startingEmpire, users, createdAt);
		this.externalId = externalId;
		this.terminated = terminated;

	}

	public boolean isTerminated() {
		return terminated;
	}

	public String getExternalId() {
		return externalId;
	}
}
