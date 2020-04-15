package fr.keyser.wonderfull.world;

import java.util.Map;

public class UnresolvedGameIdException extends WonderfullException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4694555817375667960L;
	private final String externalId;

	public UnresolvedGameIdException(String externalId) {
		super("unresolved_game");
		this.externalId = externalId;
	}

	@Override
	public void message(Map<String, Object> out) {
		super.message(out);
		out.put("externalId", externalId);
	}

}
