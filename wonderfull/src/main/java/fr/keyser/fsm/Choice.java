package fr.keyser.fsm;

public interface Choice<T> extends Edge {

	public Choice<T> when(ChoicePredicate<T> filter, Edge edge);

	public Choice<T> otherwise(Edge edge);

}
