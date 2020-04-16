package fr.keyser.fsm;

@FunctionalInterface
public interface TransitionGuard<T> {

	public static <T> TransitionGuard<T> allChildsMatch(State state) {
		return (i, t) -> i.childsInstances().stream().map(Instance::getState).allMatch(s -> state.equals(s));
	}

	public boolean guard(Instance<T> t, Transition transition);

}
