package fr.keyser.fsm.impl;

import java.util.Set;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;

public class TerminalListener<T> implements AutomatsListener<T> {

	private final Set<State> states;

	public TerminalListener(Set<State> states) {
		this.states = states;
	}

	@Override
	public Instance<T> entering(Instance<T> instance, State entered, EventMsg event) {
		return handleKill(instance, entered);
	}

	private Instance<T> handleKill(Instance<T> instance, State entered) {
		if (states.contains(entered)) {
			instance.submit(PoisonPill.kill(instance.getInstanceId()));
		}
		return instance;
	}
}
