package fr.keyser.fsm.impl;

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.keyser.fsm.ActionFunction;
import fr.keyser.fsm.Choice;
import fr.keyser.fsm.ChoicePredicate;
import fr.keyser.fsm.Container;
import fr.keyser.fsm.CreateChildFunction;
import fr.keyser.fsm.Edge;
import fr.keyser.fsm.Guard;
import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.MergeFunction;
import fr.keyser.fsm.Node;
import fr.keyser.fsm.NodeContainer;
import fr.keyser.fsm.Region;
import fr.keyser.fsm.State;
import fr.keyser.fsm.Terminal;
import fr.keyser.fsm.TimerParameter;
import fr.keyser.fsm.TransitionGuard;
import fr.keyser.fsm.TransitionNode;
import fr.keyser.fsm.impl.ChoiceListener.StateChoiceEssence;
import fr.keyser.fsm.impl.Graph.GraphBuilder;

public class AutomatsBuilder<T> implements Container<T> {

	private static class Assembler<T> {

		final Deque<AutomatsListener<T>> queue = new LinkedList<>();

		public Assembler(TerminalListener<T> terminal) {
			queue.add(terminal);
		}

		void add(UnaryOperator<AutomatsListener<T>> operator) {
			AutomatsListener<T> listener = operator.apply(peekLast());
			if (listener != null) {
				queue.add(listener);
			}
		}

		AutomatsListener<T> peekLast() {
			return queue.peekLast();
		}

	}

	private class InnerChoice extends InnerEdge implements Choice<T> {

		private final StateChoiceEssence<T> essence;

		public InnerChoice(int priority, State state) {
			super(state);
			essence = ChoiceListener.choice(priority, state, AutomatsBuilder.this::findFirstChildState);
			addChoice(essence);
		}

		@Override
		protected NodeType getType() {
			return NodeType.CHOICE;
		}

		@Override
		public Choice<T> otherwise(Edge edge) {
			essence.otherwise(edge.state());
			return this;
		}

		@Override
		protected void register(GraphBuilder assembler) {
			assembler.node(state(), getType()).transition(essence.asTransitionSource());
		}

		@Override
		public Choice<T> when(ChoicePredicate<T> filter, Edge edge) {
			essence.add(edge.state(), filter);
			return this;
		}

	}

	private class InnerJoinPoint extends InnerEdge {

		public InnerJoinPoint(State state) {
			super(state);
		}

		@Override
		protected NodeType getType() {
			return NodeType.JOINPOINT;
		}

	}

	private abstract class InnerEdge implements Edge {
		private final State state;

		public InnerEdge(State state) {
			this.state = state;
			addEdge(this);
		}

		protected InnerEdge findFirstChild() {
			return this;
		}

		protected abstract NodeType getType();

		protected void register(GraphBuilder assembler) {
			assembler.node(state(), getType());
		}

		@Override
		public State state() {
			return state;
		}

		protected State sub(String name) {
			return state().sub(name);
		}
	}

	private abstract class InnerEdgeContainer extends InnerEdge implements NodeContainer<T> {

		private Map<State, InnerEdge> edges = new LinkedHashMap<>();

		private InnerEdge initial;

		private boolean useInitial;

		protected boolean mergingAllowed;

		public InnerEdgeContainer(State state, boolean useInitial) {
			super(state);
			this.useInitial = useInitial;
		}

		protected boolean hasChild() {
			return !edges.isEmpty();
		}

		@Override
		public TransitionNode<T> auto(String name) {
			return edge(new InnerTransitionNode(sub(name)));
		}

		protected <E extends InnerEdge> E edge(E x) {
			edges.put(x.state(), x);
			if (initial == null)
				initial(x);

			return x;
		}

		@Override
		protected AutomatsBuilder<T>.InnerEdge findFirstChild() {
			if (initial != null) {
				return initial.findFirstChild();
			}

			if (!edges.isEmpty()) {
				return edges.values().iterator().next().findFirstChild();
			}
			return super.findFirstChild();
		}

		@SuppressWarnings("unchecked")
		@Override
		public NodeContainer<T> initial(Edge edge) {
			if (useInitial) {
				this.initial = (InnerEdge) edge;
			}
			return this;
		}

		@Override
		public Node<T> node(String node) {
			return edge(new InnerNode(sub(node)));
		}

		@Override
		protected void register(GraphBuilder assembler) {
			super.register(assembler);

			if (initial != null)
				assembler.node(state(), getType()).first(assembler.node(initial.state(), initial.getType()));

			if (mergingAllowed)
				assembler.node(state(), getType()).transition(MergingTransitionSource.INSTANCE);

			for (InnerEdge edge : edges.values())
				edge.register(assembler);

		}

		protected Map<State, InnerEdge> getEdges() {
			return edges;
		}
	}

	public interface ActionConfigurationSource<T> {

		public ActionConfiguration<T> asAction();
	}

	private class InnerNode extends InnerEdgeContainer implements Node<T>, ActionConfigurationSource<T> {

		private List<InnerSimpleTransition> transitions = new ArrayList<>();

		private final List<ActionFunction<T>> entry = new ArrayList<>();

		private final List<ActionFunction<T>> exit = new ArrayList<>();

		private TimerParameter timerParameter;

		private Edge timeoutEdge;

		public InnerNode(State state) {
			super(state, true);
		}

		@Override
		public InnerNode allowMerge() {
			mergingAllowed = true;
			return this;
		}

		@Override
		protected NodeType getType() {
			return hasChild() ? NodeType.CONTAINER : NodeType.LEAF;
		}

		public TimerConfiguration asTimerConfiguration(TimerRepository repository) {
			if (timerParameter == null)
				return null;
			else
				return new TimerConfiguration(state(), timerParameter, repository);
		}

		@Override
		public ActionConfiguration<T> asAction() {
			if (entry.isEmpty() && exit.isEmpty())
				return null;
			else
				return new ActionConfiguration<>(state(), entry, exit);
		}

		@Override
		public Choice<T> choice(String choice, int priority) {
			return edge(new InnerChoice(priority, sub(choice)));
		}

		@Override
		public InnerNode entry(ActionFunction<T> entry) {
			this.entry.add(entry);
			return this;
		}

		@Override
		public Guard<T> event(String event, Edge edge) {
			InnerSimpleTransition transition = new InnerSimpleTransition(this, event, edge);
			transitions.add(transition);
			return transition;
		}

		@Override
		public InnerNode timeout(TimerParameter parameter, Edge edge) {
			this.timerParameter = parameter;
			this.timeoutEdge = edge;
			return this;
		}

		@Override
		public InnerNode exit(ActionFunction<T> exit) {
			this.exit.add(exit);
			return this;
		}

		@Override
		public Region<T> region(String region, int times) {
			return edge(new InnerRegion(sub(region), times));
		}

		@Override
		protected void register(GraphBuilder assembler) {
			super.register(assembler);

			Map<String, State> to = new LinkedHashMap<>();
			for (InnerSimpleTransition ise : transitions)
				ise.feed(to);
			if (!to.isEmpty()) {
				assembler.node(state(), getType()).transition(new DirectTransitionSource(to));
			}

			if (timeoutEdge != null) {
				assembler.node(state(), getType())
						.transition(new TimeoutTransitionSource(findFirstChildState(timeoutEdge.state())));
			}

		}

		@Override
		public Terminal<T> terminal(String terminal) {
			return edge(new InnerTerminal(sub(terminal)));
		}
	}

	private class InnerRegion extends InnerEdgeContainer implements Region<T> {

		private InnerEdge joinPoint;

		private Edge joinTo;

		private final int times;

		MergeFunction<T> merging;

		CreateChildFunction<T> creating = (parent, event, index) -> null;

		public InnerRegion(State state, int times) {
			super(state, false);
			this.times = times;
			addRegion(this);
			mergingAllowed = true;
		}

		RegionConfiguration<T> asConfiguration() {

			State joinState = joinPoint != null ? joinPoint.state() : null;

			Set<State> childs = getEdges().keySet().stream().filter(s -> !s.equals(joinState))
					.collect(Collectors.toSet());

			return new RegionConfiguration<>(state(), childs, times, joinState, merging, creating);
		}

		@Override
		protected AutomatsBuilder<T>.InnerEdge findFirstChild() {
			return this;
		}

		@Override
		public Region<T> merge(MergeFunction<T> function) {
			merging = function;
			return this;
		}

		@Override
		public Region<T> create(CreateChildFunction<T> function) {
			creating = function;
			return this;
		}

		@Override
		protected NodeType getType() {
			return NodeType.region(joinPoint == null, times);
		}

		@Override
		public Edge joinTo(Edge edge) {
			joinPoint = edge(new InnerJoinPoint(sub("<join>")));
			joinTo = edge;
			return joinPoint;
		}

		@Override
		public Region<T> autoJoinTo(Edge edge) {
			joinTo = edge;
			return this;
		}

		@Override
		protected void register(GraphBuilder assembler) {
			super.register(assembler);

			assembler.node(state(), getType())
					.transition(new JoinTransitionSource(findFirstChildState(joinTo.state())));
		}

	}

	private class InnerSimpleTransition implements Guard<T> {

		private final Edge destination;

		private final String event;

		private final List<TransitionGuard<T>> guards = new ArrayList<>();

		private final InnerNode source;

		public InnerSimpleTransition(AutomatsBuilder<T>.InnerNode source, String event, Edge destination) {
			this.source = source;
			this.event = event;
			this.destination = destination;
			addTransition(this);
		}

		@Override
		public Node<T> and() {
			return source;
		}

		private void feed(Map<String, State> output) {
			output.put(event, findFirstChildState(destination.state()));
		}

		@Override
		public Guard<T> guard(TransitionGuard<T> guard) {
			guards.add(guard);
			return this;
		}
	}

	private class InnerTerminal extends InnerEdge implements Terminal<T> {

		public InnerTerminal(State state) {
			super(state);
			addTerminal(this);
		}

		@Override
		protected NodeType getType() {
			return NodeType.TERMINAL;
		}

	}

	private class InnerTransitionNode extends InnerEdge implements TransitionNode<T>, ActionConfigurationSource<T> {

		private Edge to;

		private int priority;

		private final List<ActionFunction<T>> entry = new ArrayList<>();

		private final List<ActionFunction<T>> exit = new ArrayList<>();

		public InnerTransitionNode(State state) {
			super(state);
			addAuto(this);
		}

		@Override
		public ActionConfiguration<T> asAction() {
			if (entry.isEmpty() && exit.isEmpty())
				return null;
			else
				return new ActionConfiguration<>(state(), entry, exit);
		}

		@Override
		public TransitionNode<T> to(int priority, Edge edge) {
			this.priority = priority;
			this.to = edge;
			return this;
		}

		@Override
		protected NodeType getType() {
			return NodeType.AUTO;
		}

		@Override
		public InnerTransitionNode entry(ActionFunction<T> entry) {
			this.entry.add(entry);
			return this;
		}

		@Override
		public InnerTransitionNode exit(ActionFunction<T> exit) {
			this.exit.add(exit);
			return this;
		}

		@Override
		protected void register(GraphBuilder assembler) {
			assembler.node(state(), getType()).transition(new AutoTransitionSource(findFirstChildState(to.state())));
		}

		public int getPriority() {
			return priority;
		}

	}

	private Map<State, InnerEdge> allEdges = new HashMap<>();

	private final List<InnerTransitionNode> autos = new ArrayList<>();

	private final List<StateChoiceEssence<T>> choices = new ArrayList<>();

	private Map<State, InnerEdge> edges = new LinkedHashMap<>();

	private InnerEdge initial;

	private final List<InnerRegion> regions = new ArrayList<>();

	private final List<InnerTerminal> terminals = new ArrayList<>();

	private final List<InnerSimpleTransition> transitions = new ArrayList<>();

	private TimerRepository timerRepository;

	private void addAuto(InnerTransitionNode auto) {
		this.autos.add(auto);
	}

	private void addChoice(StateChoiceEssence<T> choice) {
		choices.add(choice);
	}

	private void addTransition(InnerSimpleTransition transition) {
		transitions.add(transition);
	}

	private void addEdge(InnerEdge edge) {
		allEdges.put(edge.state(), edge);
	}

	private void addRegion(InnerRegion region) {
		regions.add(region);
	}

	private void addTerminal(InnerTerminal terminal) {
		terminals.add(terminal);
	}

	private Assembler<T> assemble() {
		Assembler<T> assembler = new Assembler<>(buildTerminal());
		assembler.add(this::buildChoice);
		assembler.add(this::buildRegion);
		assembler.add(this::buildTimers);
		assembler.add(this::buildActions);
		assembler.add(this::buildAuto);
		assembler.add(this::buildGards);
		return assembler;
	}

	@Override
	public TransitionNode<T> auto(String name) {
		return edge(new InnerTransitionNode(new State(name)));
	}

	public Automats<T> build(InstanceId id) {
		return new Automats<>(id, createGraph(), assemble().peekLast(), null);
	}

	public Automats<T> build() {
		return build(null);
	}

	private TransitionGuardListener<T> buildGards(AutomatsListener<T> next) {
		if (transitions.isEmpty())
			return null;

		Map<State, Map<String, List<TransitionGuard<T>>>> guards = new HashMap<>();
		for (InnerSimpleTransition ist : transitions) {
			State state = ist.source.state();
			Map<String, List<TransitionGuard<T>>> map = guards.computeIfAbsent(state, k -> new HashMap<>());

			map.computeIfAbsent(ist.event, k -> new ArrayList<>()).addAll(ist.guards);
		}

		return new TransitionGuardListener<>(next, guards);
	}

	private ActionListenerSource<T> buildActions(AutomatsListener<T> next) {
		List<ActionConfiguration<T>> actions = allEdges.values().stream().flatMap(e -> {
			if (e instanceof ActionConfigurationSource) {

				@SuppressWarnings("unchecked")
				ActionConfigurationSource<T> a = (ActionConfigurationSource<T>) e;
				ActionConfiguration<T> action = a.asAction();
				if (action != null)
					return Stream.of(action);
			}
			return Stream.empty();
		}).collect(Collectors.toList());

		if (actions.isEmpty())
			return null;

		return new ActionListenerSource<>(next, actions);
	}

	private TimerListenerSource<T> buildTimers(AutomatsListener<T> next) {
		List<TimerConfiguration> timers = allEdges.values().stream().flatMap(e -> {
			if (e instanceof AutomatsBuilder.InnerNode) {

				@SuppressWarnings("unchecked")
				InnerNode node = (InnerNode) e;

				// check configuration
				TimerConfiguration tc = node.asTimerConfiguration(timerRepository);
				if (tc != null)
					return Stream.of(tc);
			}
			return Stream.empty();
		}).collect(Collectors.toList());

		if (timers.isEmpty())
			return null;

		return new TimerListenerSource<>(next, timers);
	}

	private AutoListener<T> buildAuto(AutomatsListener<T> next) {
		if (autos.isEmpty()) {
			return null;
		}
		return new AutoListener<>(next,
				autos.stream().collect(Collectors.toMap(InnerTransitionNode::state, InnerTransitionNode::getPriority)));
	}

	private ChoiceListener<T> buildChoice(AutomatsListener<T> next) {
		if (choices.isEmpty()) {
			return null;
		}
		return new ChoiceListener<>(next, choices);
	}

	private RegionListener<T> buildRegion(AutomatsListener<T> next) {
		if (regions.isEmpty()) {
			return null;
		}
		return new RegionListener<>(next,
				regions.stream().map(InnerRegion::asConfiguration).collect(Collectors.toList()));
	}

	private TerminalListener<T> buildTerminal() {
		return new TerminalListener<>(terminals.stream().map(Edge::state).collect(toSet()));
	}

	@Override
	public Choice<T> choice(String choice, int priority) {
		return edge(new InnerChoice(priority, new State(choice)));
	}

	private Graph createGraph() {
		GraphBuilder builder = Graph.graph();
		edges.values().forEach(e -> e.register(builder));
		if (initial != null) {
			InnerEdge first = allEdges.get(initial.state()).findFirstChild();
			builder.first(builder.node(first.state(), first.getType()));
		}
		return builder.build();
	}

	private <E extends InnerEdge> E edge(E x) {
		edges.put(x.state(), x);
		if (initial == null)
			initial = x;
		return x;
	}

	private State findFirstChildState(State state) {
		return allEdges.get(state).findFirstChild().state();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Container<T> initial(Edge edge) {
		this.initial = (InnerEdge) edge;
		return this;
	}

	@Override
	public Node<T> node(String node) {
		return edge(new InnerNode(new State(node)));
	}

	@Override
	public Region<T> region(String region, int times) {
		return edge(new InnerRegion(new State(region), times));
	}

	@Override
	public Terminal<T> terminal(String terminal) {
		return edge(new InnerTerminal(new State(terminal)));
	}
}
