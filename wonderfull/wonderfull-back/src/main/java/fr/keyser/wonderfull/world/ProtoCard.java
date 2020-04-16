package fr.keyser.wonderfull.world;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProtoCard {

	private final int id;

	private final String name;

	@JsonCreator
	public ProtoCard(@JsonProperty("id") int id, @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("ProtoCard [id=%s, name=%s]", id, name);
	}

}
