package fr.keyser.fsm.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Transition;

public class Graph {

	public static class GraphBuilder {

		private NodeBuilder first = null;

		private Map<State, NodeBuilder> nodes = new LinkedHashMap<>();

		public Graph build() {
			return new Graph(this);
		}

		public GraphBuilder first(NodeBuilder node) {
			first = node;
			return this;
		}

		public NodeBuilder node(State state, NodeType type) {
			NodeBuilder existing = nodes.get(state);
			if (existing != null)
				return existing;

			NodeBuilder nb = new NodeBuilder(state, type);
			if (first == null)
				first = nb;
			nodes.put(state, nb);
			return nb;
		}

	}

	private static class Node {

		private final State firstChild;

		private final Graph graph;

		private final State self;

		private final NodeType type;

		private final List<TransitionSource> transitions;

		public Node(Graph graph, State self, NodeType type, State firstChild, List<TransitionSource> transitions) {
			this.graph = graph;
			this.self = self;
			this.type = type;
			this.firstChild = firstChild;
			this.transitions = transitions;
		}

		public State findFirstChild() {
			if (firstChild != null) {
				return graph.findFirstChild(firstChild);
			}

			return self;
		}

		public Stream<TransitionSource> get(EventMsg event) {
			return transitions.stream().filter(t -> t.accept(event));
		}

		@Override
		public String toString() {
			String descr = self + " " + type;
			if (transitions.isEmpty())
				return descr;
			else
				return descr + " " + transitions;
		}
	}

	public static class NodeBuilder {

		private NodeBuilder first = null;

		private Node node;

		private final State state;

		private List<TransitionSource> transitions = new ArrayList<>();

		private final NodeType type;

		public NodeBuilder(State state, NodeType type) {
			this.state = state;
			this.type = type;
		}

		public NodeBuilder child(NodeBuilder child) {
			if (first == null)
				first(child);
			return this;
		}

		private Node createNode(Graph graph) {
			if (node == null)
				node = new Node(graph, state, type, first != null ? first.state : null, new ArrayList<>(transitions));
			return node;
		}

		public NodeBuilder first(NodeBuilder child) {
			first = child;
			return this;
		}

		public NodeBuilder transition(TransitionSource transition) {
			this.transitions.add(transition);
			return this;
		}
	}

	public static GraphBuilder graph() {
		return new GraphBuilder();
	}

	private final State initialState;

	private final Map<State, Node> nodes;

	public Graph(GraphBuilder builder) {
		this.initialState = builder.first.createNode(this).self;
		this.nodes = new LinkedHashMap<>();
		for (NodeBuilder nb : builder.nodes.values()) {
			Node n = nb.createNode(this);
			this.nodes.put(n.self, n);
		}
	}

	public State findFirstChild(State state) {
		Node node = nodes.get(state);

		return node.findFirstChild();
	}

	public Collection<Transition> findTransitions(State current, EventMsg event) {
		return current.states().flatMap(s -> transitions(current, s, event)).collect(Collectors.toList());
	}

	private Stream<Transition> transitions(State root, State current, EventMsg event) {
		Node node = nodes.get(current);
		if (node == null)
			return Stream.empty();
		else {
			return node.get(event).flatMap(ts -> ts.transition(root, event));
		}
	}

	public State initial() {
		return initialState;
	}

	@Override
	public String toString() {
		return nodes.values().stream().map(Node::toString).collect(Collectors.joining("\n"));
	}

}
