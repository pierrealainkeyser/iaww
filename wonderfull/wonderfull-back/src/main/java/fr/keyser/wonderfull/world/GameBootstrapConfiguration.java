package fr.keyser.wonderfull.world;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameBootstrapConfiguration {

	public static class UserConfiguration {
		private final String uid;

		private final String empire;

		@JsonCreator
		public UserConfiguration(@JsonProperty("uid") String uid, @JsonProperty("empire") String empire) {
			this.uid = uid;
			this.empire = empire;
		}

		public String getEmpire() {
			return empire;
		}

		public String getUid() {
			return uid;
		}

		@Override
		public String toString() {
			return String.format("{uid:%s, empire:%s}", uid, empire);
		}
	}

	private final List<String> dictionaries;

	private final List<UserConfiguration> users;

	private final String startingEmpire;

	@JsonCreator
	public GameBootstrapConfiguration(@JsonProperty("dictionaries") List<String> dictionaries,
			@JsonProperty("users") List<UserConfiguration> users,
			@JsonProperty("startingEmpire") String startingEmpire) {
		this.dictionaries = dictionaries;
		this.users = users;
		this.startingEmpire = startingEmpire;
	}

	public String getStartingEmpire() {
		return startingEmpire;
	}

	public List<String> getDictionaries() {
		return dictionaries;
	}

	public List<UserConfiguration> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return String.format("GameBootstrapConfiguration [dictionaries=%s, users=%s, startingEmpire=%s]", dictionaries,
				users, startingEmpire);
	}
}
