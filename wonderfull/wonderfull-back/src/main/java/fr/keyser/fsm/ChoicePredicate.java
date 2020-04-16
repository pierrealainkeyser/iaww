package fr.keyser.fsm;

@FunctionalInterface
public interface ChoicePredicate<T> {

	public static <T> ChoicePredicate<T> allChildsMatch(State state) {
		return new ChoicePredicate<T>() {

			@Override
			public boolean test(Instance<T> instance, Transition transition) {
				return instance.childsInstances().stream().map(Instance::getState).allMatch(s -> state.equals(s));
			}

			@Override
			public String toString() {
				return "All childs match : " + state;
			}
		};
	}

	public boolean test(Instance<T> instance, Transition transition);

}
