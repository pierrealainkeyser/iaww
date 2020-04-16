package fr.keyser.fsm.impl;

import java.util.function.UnaryOperator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.State;

public class InstanceMemento {

	private final InstanceId instanceId;

	private final InstanceId parentId;

	private final Object payload;

	private final State region;

	private final State state;

	private final int index;

	@JsonCreator
	public InstanceMemento(@JsonProperty("instanceId") InstanceId instanceId,
			@JsonProperty("parentId") InstanceId parentId, @JsonProperty("payload") Object payload,
			@JsonProperty("region") State region, @JsonProperty("state") State state,
			@JsonProperty("index") int index) {
		this.instanceId = instanceId;
		this.parentId = parentId;
		this.payload = payload;
		this.region = region;
		this.state = state;
		this.index = index;
	}

	public InstanceMemento with(UnaryOperator<Object> transformation) {
		return new InstanceMemento(instanceId, parentId, transformation.apply(payload), region, state, index);
	}

	public int getIndex() {
		return index;
	}

	public InstanceId getInstanceId() {
		return instanceId;
	}

	public InstanceId getParentId() {
		return parentId;
	}

	public Object getPayload() {
		return payload;
	}

	public State getRegion() {
		return region;
	}

	public State getState() {
		return state;
	}
}
