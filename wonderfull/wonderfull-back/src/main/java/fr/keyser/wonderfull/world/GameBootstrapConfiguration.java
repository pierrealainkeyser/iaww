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

		public String getUid() {
			return uid;
		}

		public String getEmpire() {
			return empire;
		}
	}

	private final List<String> dictionaries;

	private final List<UserConfiguration> users;

	@JsonCreator
	public GameBootstrapConfiguration(@JsonProperty("dictionaries") List<String> dictionaries,
			@JsonProperty("empires") List<UserConfiguration> users) {
		this.dictionaries = dictionaries;
		this.users = users;
	}

	public List<String> getDictionaries() {
		return dictionaries;
	}

	public List<UserConfiguration> getUsers() {
		return users;
	}
}
