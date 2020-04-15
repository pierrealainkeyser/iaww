package fr.keyser.fsm;

public interface Node<T> extends Edge, Container<T>, ActionNode<T> {

	Guard<T> event(String event, Edge edge);

	Node<T> allowMerge();

	Node<T> timeout(TimerParameter parameter, Edge edge);

}
