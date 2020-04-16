package fr.keyser.wonderfull.world;

import java.util.List;

public abstract class AbstractGameDescription {

	private final List<String> dictionaries;
	private final List<String> users;

	public AbstractGameDescription(List<String> dictionaries, List<String> users) {
		this.dictionaries = dictionaries;
		this.users = users;
	}

	public List<String> getDictionaries() {
		return dictionaries;
	}

	public List<String> getUsers() {
		return users;
	}

}