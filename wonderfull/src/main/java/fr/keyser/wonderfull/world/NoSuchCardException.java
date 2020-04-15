package fr.keyser.wonderfull.world;

import java.util.Map;

public class NoSuchCardException extends WonderfullException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4095670017113373617L;
	private final int cardId;

	public NoSuchCardException(int cardId) {
		super("no_such_card");
		this.cardId = cardId;
	}

	@Override
	public void message(Map<String, Object> out) {
		super.message(out);
		out.put("card_id", cardId);
	}
}