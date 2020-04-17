package fr.keyser.wonderfull.world;

import java.time.Instant;
import java.util.List;

import fr.keyser.wonderfull.security.UserPrincipal;

public abstract class AbstractGameDescription {

	private final List<String> dictionaries;
	private final List<UserPrincipal> users;

	private final Instant createdAt;

	public AbstractGameDescription(List<String> dictionaries, List<UserPrincipal> users, Instant createdAt) {
		this.dictionaries = dictionaries;
		this.users = users;
		this.createdAt = createdAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public List<String> getDictionaries() {
		return dictionaries;
	}

	public List<UserPrincipal> getUsers() {
		return users;
	}

}