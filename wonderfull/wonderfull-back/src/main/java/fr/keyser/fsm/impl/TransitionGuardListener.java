package fr.keyser.fsm.impl;

import java.util.List;
import java.util.Map;

import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;
import fr.keyser.fsm.TransitionGuard;

public class TransitionGuardListener<T> extends DelegatedAutomatsListener<T> {

	private Map<State, Map<String, List<TransitionGuard<T>>>> guards;

	public TransitionGuardListener(AutomatsListener<T> listener,
			Map<State, Map<String, List<TransitionGuard<T>>>> guards) {
		super(listener);
		this.guards = guards;
	}

	@Override
	public boolean guard(Instance<T> instance, Transition transition) {
		Map<String, List<TransitionGuard<T>>> src = guards.get(transition.getSource());
		if (src != null) {
			List<TransitionGuard<T>> checks = src.get(transition.getEvent().getKey());
			if (checks != null) {
				return checks.stream().allMatch(g -> g.guard(instance, transition));
			}
		}
		return super.guard(instance, transition);
	}

}
