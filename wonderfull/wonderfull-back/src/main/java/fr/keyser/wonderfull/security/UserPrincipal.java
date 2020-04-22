package fr.keyser.wonderfull.security;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPrincipal implements Principal, Authentication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5126894162718534771L;

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

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@JsonIgnore
	@Override
	public Object getCredentials() {
		return "N/A";
	}

	@JsonIgnore
	@Override
	public Object getDetails() {
		return null;
	}

	@JsonIgnore
	@Override
	public Object getPrincipal() {
		return name;
	}

	@JsonIgnore
	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

	}
}
