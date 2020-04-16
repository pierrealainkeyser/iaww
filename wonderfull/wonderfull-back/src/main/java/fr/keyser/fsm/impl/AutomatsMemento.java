package fr.keyser.fsm.impl;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.fsm.InstanceId;

public class AutomatsMemento {

	private final InstanceId id;

	private final List<InstanceMemento> mementos;

	@JsonCreator
	public AutomatsMemento(@JsonProperty("id") InstanceId id,
			@JsonProperty("mementos") List<InstanceMemento> mementos) {
		this.id = id;
		this.mementos = mementos;
	}

	public AutomatsMemento map(UnaryOperator<Object> payloadMapper) {
		return new AutomatsMemento(id, mementos.stream().map(m -> m.with(payloadMapper)).collect(Collectors.toList()));
	}

	public InstanceId getId() {
		return id;
	}

	public List<InstanceMemento> getMementos() {
		return mementos;
	}
}
