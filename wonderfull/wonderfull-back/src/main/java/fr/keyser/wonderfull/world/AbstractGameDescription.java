package fr.keyser.wonderfull.world;

import java.time.Instant;
import java.util.List;

import fr.keyser.wonderfull.security.UserPrincipal;

public abstract class AbstractGameDescription {

	private final List<String> dictionaries;
	private final List<UserPrincipal> users;

	private final String creator;

	private final String startingEmpire;

	private final Instant createdAt;

	public AbstractGameDescription(String creator, List<String> dictionaries, String startingEmpire,
			List<UserPrincipal> users, Instant createdAt) {
		this.creator = creator;
		this.startingEmpire = startingEmpire;
		this.dictionaries = dictionaries;
		this.users = users;
		this.createdAt = createdAt;
	}

	public String getCreator() {
		return creator;
	}

	public String getStartingEmpire() {
		return startingEmpire;
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