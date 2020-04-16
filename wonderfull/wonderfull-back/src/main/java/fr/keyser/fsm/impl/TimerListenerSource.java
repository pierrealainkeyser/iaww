package fr.keyser.fsm.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;

public class TimerListenerSource<T> extends DelegatedAutomatsListener<T> {

	private final Map<State, TimerConfiguration> timers;

	public TimerListenerSource(AutomatsListener<T> listener, List<TimerConfiguration> timers) {
		super(listener);
		this.timers = timers.stream().collect(Collectors.toMap(TimerConfiguration::getState, Function.identity()));
	}

	@Override
	public Instance<T> entering(Instance<T> instance, State entered, EventMsg event) {

		TimerConfiguration tc = timers.get(entered);
		if (tc != null) {
			tc.register(instance.getInstanceId());
		}

		return super.entering(instance, entered, event);
	}

	@Override
	public boolean guard(Instance<T> instance, EventMsg event) {
		if (event instanceof Timeout) {
			TimerConfiguration tc = timers.get(instance.getState());
			if (tc != null) {
				Timeout timeout = (Timeout) event;
				return tc.match(timeout.getPayload());
			}
			return false;
		}

		return super.guard(instance, event);
	}

	@Override
	public Instance<T> leaving(Instance<T> instance, State leaved, EventMsg event) {

		TimerConfiguration tc = timers.get(leaved);
		if (tc != null) {
			tc.unregister(instance.getInstanceId());
		}

		return super.leaving(instance, leaved, event);
	}

}
