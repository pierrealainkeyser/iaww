package fr.keyser.fsm.impl;

import java.util.Collections;
import java.util.Set;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.InstanceId;

public class Choice extends EventMsg {

	public static Choice choice(InstanceId target) {
		return new Choice("<choice>", Collections.singleton(target), false);
	}

	private final boolean otherwise;

	private Choice(String key, Set<InstanceId> target, boolean otherwise) {
		super(key, target, null);
		this.otherwise = otherwise;
	}

	public Choice build(int index, boolean otherwise) {
		return new Choice(index + "", getTargets(), otherwise);

	}

	public int getIndex() {
		return Integer.parseInt(getKey());
	}

	public boolean isOtherwise() {
		return otherwise;
	}

}
