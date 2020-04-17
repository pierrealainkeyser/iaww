package fr.keyser.wonderfull.world;

import java.time.Instant;
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

	private final Instant createdAt;

	@JsonCreator
	public GameBootstrapConfiguration(@JsonProperty("dictionaries") List<String> dictionaries,
			@JsonProperty("empires") List<UserConfiguration> users, @JsonProperty("createdAt") Instant createdAt) {
		this.dictionaries = dictionaries;
		this.users = users;
		this.createdAt = createdAt;
	}

	public List<String> getDictionaries() {
		return dictionaries;
	}

	public List<UserConfiguration> getUsers() {
		return users;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
