package fr.keyser.wonderfull.world;

import java.util.List;

public class PlayerGameDescription extends AbstractGameDescription {

	private final String externalId;

	private final boolean terminated;

	public PlayerGameDescription(String externalId, boolean terminated, List<String> dictionaries, List<String> users) {
		super(dictionaries, users);
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
