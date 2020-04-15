package fr.keyser.fsm;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;

public class InstanceId {

	public static InstanceId uuid() {
		return new InstanceId(UUID.randomUUID().toString());
	}

	private final String id;

	@JsonCreator
	public InstanceId(@JsonUnwrapped String id) {
		this.id = id;
	}

	@JsonValue
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InstanceId))
			return false;
		InstanceId other = (InstanceId) obj;
		return Objects.equals(id, other.id);
	}

}
