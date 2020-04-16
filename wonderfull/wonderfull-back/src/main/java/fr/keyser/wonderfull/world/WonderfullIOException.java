package fr.keyser.wonderfull.world;

import java.io.IOException;
import java.util.Map;

public class WonderfullIOException extends WonderfullException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8632013965727219958L;
	private final String message;

	public WonderfullIOException(IOException io) {
		super("exception", io);
		this.message = io.getMessage();
	}

	@Override
	public void message(Map<String, Object> out) {
		super.message(out);
		out.put("message", message);
	}

}
