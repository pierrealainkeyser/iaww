package fr.keyser.fsm.impl;

import java.util.ArrayList;
import java.util.List;

import fr.keyser.fsm.ActionFunction;
import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;

public class ActionConfiguration<T> {

	private final List<ActionFunction<T>> entry;

	private final List<ActionFunction<T>> exit;

	private final State state;

	public ActionConfiguration(State state, List<ActionFunction<T>> entry, List<ActionFunction<T>> exit) {
		this.state = state;
		this.entry = new ArrayList<>(entry);
		this.exit = new ArrayList<>(exit);
	}

	public Instance<T> entering(Instance<T> current, EventMsg event) {
		for (ActionFunction<T> e : entry)
			current = e.action(current, event);
		return current;
	}

	public Instance<T> leaving(Instance<T> current, EventMsg event) {
		for (ActionFunction<T> e : exit)
			current = e.action(current, event);
		return current;
	}

	public State getState() {
		return state;
	}

}
