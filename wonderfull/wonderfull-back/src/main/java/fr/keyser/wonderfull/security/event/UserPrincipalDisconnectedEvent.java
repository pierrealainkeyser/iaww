package fr.keyser.wonderfull.security.event;

import fr.keyser.wonderfull.security.UserPrincipal;

public class UserPrincipalDisconnectedEvent extends UserPrincipalEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -263702913580966533L;

	public UserPrincipalDisconnectedEvent(UserPrincipal source) {
		super(source);
	}

}
