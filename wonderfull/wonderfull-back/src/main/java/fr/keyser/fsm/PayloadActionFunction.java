package fr.keyser.fsm;

@FunctionalInterface
public interface PayloadActionFunction<T> {

	public T action(T current, EventMsg event);

	public default ActionFunction<T> asAction() {
		return (i, event) -> i.update(p -> action(p, event));
	}

}
