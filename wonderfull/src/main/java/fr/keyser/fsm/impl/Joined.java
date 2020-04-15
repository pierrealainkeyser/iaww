package fr.keyser.fsm.impl;

import java.util.Collections;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.RegionEvent;

public class Joined extends EventMsg implements RegionEvent {

	public static final String KEY = "<joined>";

	private Joined(InstanceId target) {
		super(KEY, Collections.singleton(target), null);
	}

	public final static Joined join(InstanceId id) {
		return new Joined(id);
	}

}
