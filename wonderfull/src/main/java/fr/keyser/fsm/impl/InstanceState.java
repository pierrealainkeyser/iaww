package fr.keyser.fsm.impl;

import java.util.Objects;

import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.State;

public class InstanceState {

	private final InstanceId id;

	private final State state;

	public InstanceState(InstanceId id, State state) {
		this.id = id;
		this.state = state;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InstanceState))
			return false;
		InstanceState other = (InstanceState) obj;
		return Objects.equals(id, other.id) && Objects.equals(state, other.state);
	}

	public InstanceId getId() {
		return id;
	}
}
