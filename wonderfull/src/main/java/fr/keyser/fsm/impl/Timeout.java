package fr.keyser.fsm.impl;

import java.util.Collections;

import fr.keyser.fsm.EventMsg;

public class Timeout extends EventMsg {

	public static final String KEY = "<timeout>";

	private Timeout(RegisteredTimer timer) {
		super(KEY, Collections.singleton(timer.getId()), timer);
	}

	@Override
	public RegisteredTimer getPayload() {
		return (RegisteredTimer) super.getPayload();
	}

	public final static Timeout timeout(RegisteredTimer timer) {
		return new Timeout(timer);
	}

}
