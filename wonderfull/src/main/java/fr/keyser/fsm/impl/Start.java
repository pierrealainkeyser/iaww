package fr.keyser.fsm.impl;

import fr.keyser.fsm.EventMsg;

public class Start extends EventMsg {

	public final static Start start(Object payload) {
		return new Start("<start>", payload);
	}

	private Start(String key, Object payload) {
		super(key, null, payload);
	}

}
