package fr.keyser.fsm.impl;

import java.util.Objects;

import fr.keyser.fsm.InstanceId;

public class RegisteredTimer {

	private final InstanceState instanceState;

	private final int index;

	public RegisteredTimer(InstanceState id) {
		this(id, 0);
	}

	private RegisteredTimer(InstanceState id, int index) {
		this.instanceState = id;
		this.index = index;
	}

	public RegisteredTimer next() {
		return new RegisteredTimer(instanceState, index + 1);
	}

	public boolean match(RegisteredTimer other) {
		return Objects.equals(instanceState, other.instanceState) && Objects.equals(index, other.index);
	}

	public InstanceState getInstanceState() {
		return instanceState;
	}

	public InstanceId getId() {
		return instanceState.getId();
	}
}
