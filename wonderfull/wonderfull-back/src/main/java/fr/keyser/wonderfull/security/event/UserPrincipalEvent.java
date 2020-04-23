package fr.keyser.wonderfull.security.event;

import org.springframework.context.ApplicationEvent;

import fr.keyser.wonderfull.security.UserPrincipal;

public abstract class UserPrincipalEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7378404742974824637L;

	public UserPrincipalEvent(UserPrincipal source) {
		super(source);
	}

	@Override
	public UserPrincipal getSource() {
		return (UserPrincipal) super.getSource();
	}

}
