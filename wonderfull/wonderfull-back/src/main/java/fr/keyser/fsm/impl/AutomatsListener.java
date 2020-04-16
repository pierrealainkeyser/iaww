package fr.keyser.fsm.impl;

import java.util.function.Function;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;

public interface AutomatsListener<T> {

	public default Instance<T> leaving(Instance<T> instance, State leaved, EventMsg event) {
		return instance;
	}

	public default Instance<T> entering(Instance<T> instance, State entered, EventMsg event) {
		return instance;
	}

	public default T start(Instance<T> parent, State enteredState, EventMsg event, int index) {
		return parent.opt(Function.identity()).orElse(null);
	}

	public default boolean guard(Instance<T> instance, Transition transition) {
		return true;
	}

	public default boolean guard(Instance<T> instance, EventMsg event) {
		return true;
	}

	public default Instance<T> terminating(Instance<T> instance) {
		return instance;
	}

	public default void reload(Instance<T> instance) {

	}

	@SuppressWarnings("unchecked")
	public default T start(Start start) {
		return (T) start.getPayload();
	}

}
