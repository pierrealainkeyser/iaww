package fr.keyser.fsm.impl;

import java.util.stream.Stream;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;

public interface TransitionSource {

	boolean accept(EventMsg event);

	Stream<Transition> transition(State source, EventMsg event);

}