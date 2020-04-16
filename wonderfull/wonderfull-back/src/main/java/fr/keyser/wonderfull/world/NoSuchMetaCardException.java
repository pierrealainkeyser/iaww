package fr.keyser.wonderfull.world;

import java.util.Map;

public class NoSuchMetaCardException extends WonderfullException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4095670017113373617L;
	private final String name;

	public NoSuchMetaCardException(String name) {
		super("no_such_metacard");
		this.name = name;
	}

	@Override
	public void message(Map<String, Object> out) {
		super.message(out);
		out.put("card_name", name);
	}
}