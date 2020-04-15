package fr.keyser.fsm;

public interface TransitionNode<T> extends Edge, ActionNode<T> {

	public default TransitionNode<T> to(Edge edge) {
		return to(0, edge);
	}

	public TransitionNode<T> to(int priority, Edge edge);

}
