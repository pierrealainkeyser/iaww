package fr.keyser.wonderfull.world.game;

import java.util.Map;

import fr.keyser.wonderfull.world.WonderfullException;

public class IllegalActionException extends WonderfullException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4983701953193820945L;

	private final String action;

	public IllegalActionException(String action) {
		super("illegal_action");
		this.action = action;
	}

	@Override
	public void message(Map<String, Object> out) {
		super.message(out);
		out.put("action", action);
	}

}
