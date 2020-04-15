package fr.keyser.fsm;

import java.util.Collections;

public class Merge extends EventMsg implements RegionEvent {

	private final State region;

	public final static Merge merge(InstanceId id, Object payload, State region) {
		return new Merge(id, payload, region);
	}

	private Merge(InstanceId target, Object payload, State region) {
		super("<merge>", Collections.singleton(target), payload);
		this.region = region;
	}

	public State getRegion() {
		return region;
	}

}
