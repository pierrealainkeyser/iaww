package fr.keyser.fsm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.State;

class AutomatInstance<T> implements Instance<T> {

	static <T> AutomatInstance<T> initial(Automats<T> container, InstanceId instanceId, State state, T payload) {
		return new AutomatInstance<T>(container, instanceId, state, payload, null, null, -1);
	}

	private final Automats<T> container;

	private final InstanceId instanceId;

	private final InstanceId parentId;

	private final T payload;

	private final State region;

	private final State state;

	private final int index;

	private AutomatInstance(Automats<T> container, InstanceId instanceId, State state, T payload, InstanceId parentId,
			State region, int index) {
		this.container = container;
		this.instanceId = instanceId;
		this.state = state;
		this.payload = payload;
		this.parentId = parentId;
		this.region = region;
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	AutomatInstance(Automats<T> container, InstanceMemento memento) {
		this(container, memento.getInstanceId(), memento.getState(), (T) memento.getPayload(), memento.getParentId(),
				memento.getRegion(), memento.getIndex());
	}

	public InstanceMemento memento() {
		return new InstanceMemento(instanceId, parentId, payload, region, state, index);
	}

	AutomatInstance<T> child(InstanceId instanceId, State state, T payload, State enteredState, int index) {
		return new AutomatInstance<>(container, instanceId, state, payload, this.instanceId, enteredState, index);
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public List<Instance<T>> childsInstances() {
		return container.childsOf(this.getInstanceId());
	}

	@Override
	public <X> Optional<X> opt(Function<T, X> getter) {
		return Optional.ofNullable(payload).map(getter);
	}

	@Override
	public InstanceId getInstanceId() {
		return instanceId;
	}

	@Override
	public Instance<T> getParent() {
		if (parentId == null)
			return null;

		return container.getInstance(parentId);
	}

	InstanceId getParentId() {
		return parentId;
	}

	@Override
	public State getRegion() {
		return region;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public void startChild(State enteredState, State childState, EventMsg start, int index) {
		container.startChild(this, enteredState, childState, start, index);
	}

	AutomatInstance<T> state(State state) {
		return new AutomatInstance<T>(container, instanceId, state, payload, parentId, region, index);
	}

	@Override
	public void submit(int index, EventMsg event) {
		container.submit(index, event);

	}

	@Override
	public String toString() {

		List<String> msg = new ArrayList<>();
		msg.add("instanceId=" + instanceId);
		if (index >= 0)
			msg.add("index=" + index);
		msg.add("state=" + state);
		if (payload != null)
			msg.add("payload=" + payload);

		if (parentId != null)
			msg.add("parentId=" + parentId);

		if (region != null)
			msg.add("region=" + region);

		return msg.stream().collect(Collectors.joining(", "));
	}

	@Override
	public AutomatInstance<T> update(UnaryOperator<T> operator) {
		return new AutomatInstance<T>(container, instanceId, state, operator.apply(payload), parentId, region, index);
	}
}
