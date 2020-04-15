package fr.keyser.fsm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;

public final class State {

	private final List<String> states;

	private final String inner;

	@JsonCreator
	public State(@JsonUnwrapped List<String> states) {
		this.states = states;
		this.inner = this.states.stream().collect(Collectors.joining("/"));
	}

	public State(String... states) {
		this(Arrays.asList(states));
	}

	@JsonValue
	public List<String> values() {
		return Collections.unmodifiableList(states);
	}

	public Stream<State> diff(State other, boolean reversed) {
		int mySize = states.size();
		int otherSize = other.states.size();
		int common = 0;
		boolean done = false;
		while (common < mySize && common < otherSize && !done) {
			if (!states.get(common).equals(other.states.get(common))) {
				done = true;
			} else
				++common;
		}

		int to = mySize + 1;
		int from = common + 1;
		IntStream range = IntStream.range(from, to);
		if (reversed)
			range = range.map(i -> to - i + from - 1);
		return range.mapToObj(this::subState);
	}

	public Stream<State> states() {
		IntStream range = IntStream.range(1, states.size() + 1);
		return range.mapToObj(this::subState);
	}

	private State subState(int index) {
		return new State(states.subList(0, index));
	}

	public State sub(String state) {
		List<String> states = new ArrayList<>(this.states.size() + 1);
		states.addAll(this.states);
		states.add(state);
		return new State(states);
	}

	@Override
	public String toString() {
		return inner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inner == null) ? 0 : inner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (inner == null) {
			if (other.inner != null)
				return false;
		} else if (!inner.equals(other.inner))
			return false;
		return true;
	}
}
