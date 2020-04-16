package fr.keyser.fsm.impl;

import java.util.stream.Stream;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;

public class TimeoutTransitionSource implements TransitionSource {

	private final State destination;

	public TimeoutTransitionSource(State destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "<timeout> -> " + destination;
	}

	@Override
	public boolean accept(EventMsg event) {
		return event instanceof Timeout;
	}

	@Override
	public Stream<Transition> transition(State source, EventMsg event) {
		return Stream.of(new Transition(source, event, destination));
	}

}
