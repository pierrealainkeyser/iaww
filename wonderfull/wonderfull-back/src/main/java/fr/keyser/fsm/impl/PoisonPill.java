package fr.keyser.fsm.impl;

import java.util.Collections;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.InstanceId;

public class PoisonPill extends EventMsg {

	public static final PoisonPill ALL = new PoisonPill();

	public static final PoisonPill kill(InstanceId id) {
		return new PoisonPill(id);
	}

	private PoisonPill() {
		super("<poison>", null, null);
	}

	private PoisonPill(InstanceId target) {
		super("<poison>", Collections.singleton(target), null);
	}

}
