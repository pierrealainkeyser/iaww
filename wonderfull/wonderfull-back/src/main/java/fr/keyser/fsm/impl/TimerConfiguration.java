package fr.keyser.fsm.impl;

import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.State;
import fr.keyser.fsm.TimerParameter;

public class TimerConfiguration {

	private final State state;

	private final TimerParameter parameter;

	private final TimerRepository repository;

	public TimerConfiguration(State state, TimerParameter parameter, TimerRepository repository) {
		this.state = state;
		this.parameter = parameter;
		this.repository = repository;
	}

	public State getState() {
		return state;
	}

	public boolean match(RegisteredTimer timer) {
		return repository.match(timer);
	}

	public void register(InstanceId id) {
		repository.register(new InstanceState(id, state), parameter);
	}

	public void unregister(InstanceId id) {
		repository.unregister(new InstanceState(id, state));
	}

}
