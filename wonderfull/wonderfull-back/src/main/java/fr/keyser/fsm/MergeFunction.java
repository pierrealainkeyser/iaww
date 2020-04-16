package fr.keyser.fsm;

@FunctionalInterface
public interface MergeFunction<T> {

	public T merge(T current, Object payload);

}
