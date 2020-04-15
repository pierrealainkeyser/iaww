package fr.keyser.fsm;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.keyser.fsm.State;

class TestState {

	@Test
	void testDiffReverse() {
		State src = new State("A", "B", "C");
		State dest = new State("E", "A");

		List<State> diffSrcDest = src.diff(dest, true).collect(toList());
		assertIterableEquals(asList(new State("A", "B", "C"), new State("A", "B"), new State("A")), diffSrcDest);
		List<State> diffDestSrc = dest.diff(src, true).collect(toList());
		assertIterableEquals(asList(new State("E", "A"), new State("E")), diffDestSrc);
	}

	@Test
	void testDiffPlain() {
		State src = new State("A", "B", "C");
		State dest = new State("A", "E");

		List<State> diffSrcDest = src.diff(dest, false).collect(toList());
		assertIterableEquals(asList(new State("A", "B"), new State("A", "B", "C")), diffSrcDest);
		List<State> diffDestSrc = dest.diff(src, false).collect(toList());
		assertIterableEquals(asList(new State("A", "E")), diffDestSrc);
	}

	@Test
	void testStates() {
		assertIterableEquals(asList(new State("A"), new State("A", "B"), new State("A", "B", "C")),
				new State("A", "B", "C").states().collect(toList()));

		assertIterableEquals(asList(new State("A")), new State("A").states().collect(toList()));
	}

}
