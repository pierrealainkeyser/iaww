package fr.keyser.fsm.impl;

import java.util.Collections;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.InstanceId;

public class Auto extends EventMsg {

	public static final String KEY = "<auto>";

	private Auto(InstanceId target) {
		super(KEY, Collections.singleton(target), null);
	}

	public final static Auto auto(InstanceId id) {
		return new Auto(id);
	}

}
