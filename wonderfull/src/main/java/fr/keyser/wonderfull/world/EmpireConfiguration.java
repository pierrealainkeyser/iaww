package fr.keyser.wonderfull.world;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmpireConfiguration {

	private final String user;

	private final String externalId;

	private final String empire;

	public static EmpireConfiguration empire(String player, String empire) {
		return new EmpireConfiguration(player, null, empire);
	}

	@JsonCreator
	public EmpireConfiguration(@JsonProperty("user") String user, @JsonProperty("externalId") String externalId,
			@JsonProperty("empire") String empire) {
		this.user = user;
		this.externalId = externalId;
		this.empire = empire;
	}

	public EmpireConfiguration withRandomUUID() {
		return new EmpireConfiguration(user, UUID.randomUUID().toString(), empire);
	}

	public String getUser() {
		return user;
	}

	public String getEmpire() {
		return empire;
	}

	public String getExternalId() {
		return externalId;
	}

	@Override
	public String toString() {
		return String.format("{user=%s, externalId=%s, empire=%s}", user, externalId, empire);
	}

}
