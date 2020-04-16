package fr.keyser.wonderfull.world;

import java.util.Map;

public abstract class WonderfullException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4299962064182468076L;

	public WonderfullException(String message) {
		super(message);
	}

	public WonderfullException(String message, Throwable other) {
		super(message, other);
	}

	public void message(Map<String, Object> out) {
		out.put("code", getMessage());
	}

}
