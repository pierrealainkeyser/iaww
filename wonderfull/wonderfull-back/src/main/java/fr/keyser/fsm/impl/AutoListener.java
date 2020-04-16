package fr.keyser.fsm.impl;

import java.util.Map;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;

public class AutoListener<T> extends DelegatedAutomatsListener<T> {

	private final Map<State, Integer> states;

	public AutoListener(AutomatsListener<T> listener, Map<State, Integer> states) {
		super(listener);
		this.states = states;
	}

	@Override
	public Instance<T> entering(Instance<T> instance, State entered, EventMsg event) {
		if (states.containsKey(entered)) {
			int priority = states.get(entered);
			instance.submit(priority, Auto.auto(instance.getInstanceId()));
		}
		return super.entering(instance, entered, event);
	}
}
