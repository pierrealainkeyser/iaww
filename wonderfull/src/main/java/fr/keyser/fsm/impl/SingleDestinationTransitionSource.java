package fr.keyser.fsm.impl;

import java.util.stream.Stream;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;

public abstract class SingleDestinationTransitionSource implements TransitionSource {

	protected final State destination;

	public SingleDestinationTransitionSource(State destination) {
		this.destination = destination;
	}

	@Override
	public Stream<Transition> transition(State source, EventMsg event) {
		return Stream.of(new Transition(source, event, destination));
	}

}