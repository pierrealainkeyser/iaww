package fr.keyser.fsm;

@FunctionalInterface
public interface ActionFunction<T> {

	public Instance<T> action(Instance<T> current, EventMsg event);

}
