package fr.keyser.fsm;

@FunctionalInterface
public interface CreateChildFunction<T> {

	public T createChild(T parent, EventMsg event, int index);

}
