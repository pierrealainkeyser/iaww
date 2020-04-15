package fr.keyser.fsm.impl;

import java.util.Set;

import fr.keyser.fsm.CreateChildFunction;
import fr.keyser.fsm.EventMsg;
import fr.keyser.fsm.Instance;
import fr.keyser.fsm.MergeFunction;
import fr.keyser.fsm.State;

public class RegionConfiguration<T> {

	private final State region;

	private final int times;

	private final Set<State> states;

	private final State joinState;

	private final MergeFunction<T> merging;

	private final CreateChildFunction<T> creating;

	public RegionConfiguration(State region, Set<State> states, int times, State joinState, MergeFunction<T> merging,
			CreateChildFunction<T> creating) {
		this.region = region;
		this.states = states;
		this.times = times;
		this.joinState = joinState;
		this.merging = merging;
		this.creating = creating;
	}

	public boolean isAutoJoin() {
		return joinState == null;
	}

	public int getTotal() {
		return times * states.size();
	}

	public int getTimes() {
		return times;
	}

	public Set<State> getStates() {
		return states;
	}

	public State getJoinState() {
		return joinState;
	}

	public State getRegion() {
		return region;
	}

	public Instance<T> merge(Instance<T> instance, Object payload) {
		return instance.update(v -> merging.merge(v, payload));
	}

	public T createChild(T parent, EventMsg event, int index) {
		return creating.createChild(parent, event, index);
	}
}
