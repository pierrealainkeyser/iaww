package fr.keyser.fsm.impl;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.State;

public class JoinTransitionSource extends SingleDestinationTransitionSource {

	public JoinTransitionSource(State destination) {
		super(destination);
	}

	@Override
	public String toString() {
		return Joined.KEY + " -> " + destination;
	}

	@Override
	public boolean accept(EventMsg event) {
		return event instanceof Joined;
	}

}
