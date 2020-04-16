package fr.keyser.fsm.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;

public class ActionListenerSource<T> extends DelegatedAutomatsListener<T> {

	private final Map<State, ActionConfiguration<T>> actions;

	public ActionListenerSource(AutomatsListener<T> listener, List<ActionConfiguration<T>> actions) {
		super(listener);
		this.actions = actions.stream().collect(Collectors.toMap(ActionConfiguration::getState, Function.identity()));
	}

	@Override
	public Instance<T> entering(Instance<T> instance, State entered, EventMsg event) {

		ActionConfiguration<T> a = actions.get(entered);
		if (a != null)
			instance = a.entering(instance, event);

		return super.entering(instance, entered, event);
	}

	@Override
	public Instance<T> leaving(Instance<T> instance, State leaved, EventMsg event) {
		ActionConfiguration<T> a = actions.get(leaved);
		if (a != null)
			instance = a.leaving(instance, event);

		return super.leaving(instance, leaved, event);
	}

}
