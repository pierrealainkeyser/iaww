package fr.keyser.fsm;

public interface Guard<T> {

	public Guard<T> guard(TransitionGuard<T> guard);

	public Node<T> and();

}
