package fr.keyser.wonderfull.security;

import java.security.Principal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPrincipal implements Principal {
	private final String name;

	private final String label;

	@JsonCreator
	public UserPrincipal(@JsonProperty("name") String name, @JsonProperty("label") String label) {
		this.name = name;
		this.label = label;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return String.format("UserPrincipal [name=%s, label=%s]", name, label);
	}
}
