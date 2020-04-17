package fr.keyser.wonderfull.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPrincipal {
	private final String name;

	private final String uid;

	@JsonCreator
	public UserPrincipal(@JsonProperty("name") String name, @JsonProperty("uid") String uid) {
		this.name = name;
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public String getUid() {
		return uid;
	}

	@Override
	public String toString() {
		return String.format("UserPrincipal [name=%s, uid=%s]", name, uid);
	}
}
