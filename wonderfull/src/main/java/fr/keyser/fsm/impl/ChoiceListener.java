package fr.keyser.fsm.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.keyser.fsm.ChoicePredicate;
import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;

public class ChoiceListener<T> extends DelegatedAutomatsListener<T> {

	private static class Destination<T> {
		private final State destination;

		private final ChoicePredicate<T> filter;

		public Destination(State destination, ChoicePredicate<T> filter) {
			this.destination = destination;
			this.filter = filter;
		}

		public State getDestination() {
			return destination;
		}

		@Override
		public String toString() {
			if (filter == null)
				return " -> " + destination;
			else
				return filter + " -> " + destination;
		}

		public boolean test(Instance<T> t, Transition transition) {
			return filter.test(t, transition);
		}

	}

	private static class StateChoice<T> implements TransitionSource {

		private final int priority;
		private final Map<Integer, Destination<T>> choices;

		private StateChoice(int priority, Map<Integer, Destination<T>> choices) {
			this.priority = priority;
			this.choices = choices;
		}

		@Override
		public String toString() {

			return choices.values().stream().map(Destination::toString).collect(Collectors.joining(", "));
		}

		public boolean test(Choice choice, Instance<T> instance, Transition transition) {

			Destination<T> predicate = choices.get(choice.getIndex());
			if (predicate == null) {
				return false;
			}

			return predicate.test(instance, transition);
		}

		private Stream<Transition> transition(State source, Choice choice) {

			Stream.Builder<Transition> builder = Stream.builder();
			Iterator<Entry<Integer, Destination<T>>> it = choices.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Destination<T>> next = it.next();
				Integer key = next.getKey();
				Destination<T> destination = next.getValue();

				Choice newChoice = choice.build(key, !it.hasNext());

				builder.add(new Transition(source, newChoice, destination.getDestination()));
			}

			return builder.build();
		}

		@Override
		public boolean accept(EventMsg event) {
			return event instanceof Choice;
		}

		@Override
		public Stream<Transition> transition(State source, EventMsg event) {
			return transition(source, (Choice) event);
		}

		public int getPriority() {
			return priority;
		}
	}

	public static <T> StateChoiceEssence<T> choice(int priority, State source, Function<State, State> stateEncoder) {
		return new StateChoiceEssence<>(priority, source, stateEncoder);
	}

	public static class StateChoiceEssence<T> {

		private final List<Destination<T>> destinations = new ArrayList<>();

		private final State source;

		private State otherwise;

		private StateChoice<T> choice;

		private final Function<State, State> stateEncoder;

		private final int priority;

		public StateChoiceEssence(int priority, State source, Function<State, State> stateEncoder) {
			this.priority = priority;
			this.source = source;
			this.stateEncoder = stateEncoder;
		}

		public StateChoiceEssence<T> add(State destination, ChoicePredicate<T> filter) {
			destinations.add(new Destination<>(destination, filter));
			return this;
		}

		public TransitionSource asTransitionSource() {
			return getDestinations();
		}

		private StateChoice<T> getDestinations() {
			if (choice == null) {

				Map<Integer, Destination<T>> choices = new TreeMap<>();
				int i = 0;
				for (Destination<T> dest : destinations) {
					choices.put(i++, new Destination<>(stateEncoder.apply(dest.destination), dest.filter));
				}
				if (otherwise != null) {
					choices.put(i, new Destination<>(stateEncoder.apply(otherwise), null));
				}
				choice = new StateChoice<>(priority, choices);
			}
			return choice;
		}

		private State getSource() {
			return source;
		}

		public StateChoiceEssence<T> otherwise(State otherwise) {
			this.otherwise = otherwise;
			return this;
		}

	}

	private final Map<State, StateChoice<T>> states;

	public ChoiceListener(AutomatsListener<T> listener, List<StateChoiceEssence<T>> essences) {
		super(listener);

		states = essences.stream()
				.collect(Collectors.toMap(StateChoiceEssence::getSource, StateChoiceEssence::getDestinations));
	}

	@Override
	public Instance<T> entering(Instance<T> instance, State entered, EventMsg event) {
		if (handleChoice(instance, entered))
			return instance;
		else
			return super.entering(instance, entered, event);
	}

	@Override
	public boolean guard(Instance<T> instance, Transition transition) {
		EventMsg event = transition.getEvent();
		if (event instanceof Choice) {

			Choice choice = (Choice) event;

			if (choice.isOtherwise())
				return true;
			else {

				State source = transition.getSource();
				StateChoice<T> stateChoice = states.get(source);
				if (states != null) {
					return stateChoice.test(choice, instance, transition);
				}
			}

			return false;
		}

		return super.guard(instance, transition);
	}

	private boolean handleChoice(Instance<T> instance, State entered) {
		StateChoice<T> stateChoice = states.get(entered);
		if (stateChoice != null) {
			instance.submit(stateChoice.getPriority(), Choice.choice(instance.getInstanceId()));
			return true;
		}
		return false;
	}
}
