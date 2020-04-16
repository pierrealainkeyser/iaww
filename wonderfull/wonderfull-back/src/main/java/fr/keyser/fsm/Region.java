package fr.keyser.fsm;

public interface Region<T> extends Edge, NodeContainer<T> {

	Edge joinTo(Edge edge);

	Region<T> autoJoinTo(Edge edge);

	Region<T> merge(MergeFunction<T> function);

	Region<T> create(CreateChildFunction<T> function);
}
