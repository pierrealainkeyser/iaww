package fr.keyser.wonderfull.world;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.security.UserPrincipal;

public class EmpireConfiguration {

	private final UserPrincipal user;

	private final String externalId;

	private final String empire;

	public static EmpireConfiguration empire(UserPrincipal player, String empire) {
		return new EmpireConfiguration(player, player.getName(), empire);
	}

	@JsonCreator
	public EmpireConfiguration(@JsonProperty("user") UserPrincipal user, @JsonProperty("externalId") String externalId,
			@JsonProperty("empire") String empire) {
		this.user = user;
		this.externalId = externalId;
		this.empire = empire;
	}

	public UserPrincipal getUser() {
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
