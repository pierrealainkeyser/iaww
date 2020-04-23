package fr.keyser.wonderfull.security.event;

import fr.keyser.wonderfull.security.UserPrincipal;

public class UserPrincipalConnectedEvent extends UserPrincipalEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -263702913580966533L;

	public UserPrincipalConnectedEvent(UserPrincipal source) {
		super(source);
	}

}
