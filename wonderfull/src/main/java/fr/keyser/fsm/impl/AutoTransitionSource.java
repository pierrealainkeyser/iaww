package fr.keyser.fsm.impl;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.State;

public class AutoTransitionSource extends SingleDestinationTransitionSource {

	public AutoTransitionSource(State destination) {
		super(destination);
	}

	@Override
	public String toString() {
		return Auto.KEY + " -> " + destination;
	}

	@Override
	public boolean accept(EventMsg event) {
		return event instanceof Auto;
	}

}
