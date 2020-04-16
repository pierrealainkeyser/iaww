package fr.keyser.fsm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EventMsg {

	private final String key;

	private final Object payload;

	private final Set<InstanceId> targets;

	public static EventMsg multicast(String key, Object payload, Set<InstanceId> targets) {
		return new EventMsg(key, targets, payload);
	}

	public static EventMsg unicast(String key, InstanceId target, Object payload) {
		return new EventMsg(key, Collections.singleton(target), payload);
	}

	public static EventMsg unicast(String key, InstanceId target) {
		return unicast(key, target, null);
	}

	public static EventMsg broadcast(String key, Object payload) {
		return new EventMsg(key, null, payload);
	}

	public static EventMsg broadcast(String key) {
		return broadcast(key, null);
	}

	public EventMsg(String key, Set<InstanceId> targets, Object payload) {
		this.key = key;
		this.targets = targets == null ? Collections.emptySet() : Collections.unmodifiableSet(targets);
		this.payload = payload;
	}

	public String getKey() {
		return key;
	}

	public Object getPayload() {
		return payload;
	}

	public Set<InstanceId> getTargets() {
		return targets;
	}

	@Override
	public String toString() {
		List<String> str = new ArrayList<>();
		if (key != null)
			str.add("key=" + key);
		if (targets != null)
			str.add("targets=" + targets);
		if (payload != null)
			str.add("payload");

		String name = getClass().getSimpleName();
		return name + str;
	}

}
