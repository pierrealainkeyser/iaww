package fr.keyser.fsm.impl;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;

public class DirectTransitionSource implements TransitionSource {

	private Map<String, State> to;

	public DirectTransitionSource(Map<String, State> to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return to.entrySet().stream().map(e -> "@" + e.getKey() + " -> " + e.getValue())
				.collect(Collectors.joining(", "));
	}

	@Override
	public boolean accept(EventMsg event) {
		return true;
	}

	@Override
	public Stream<Transition> transition(State source, EventMsg event) {

		State destination = to.get(event.getKey());
		if (destination == null)
			return Stream.empty();
		return Stream.of(new Transition(source, event, destination));
	}

}
