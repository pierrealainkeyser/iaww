package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventAt {
	private final int at;

	private final int player;

	private final EmpireEvent event;

	@JsonCreator
	public EventAt(@JsonProperty("at") int at, @JsonProperty("player") int player,
			@JsonProperty("event") EmpireEvent event) {
		this.at = at;
		this.player = player;
		this.event = event;
	}

	public int getPlayer() {
		return player;
	}

	public int getAt() {
		return at;
	}

	public EmpireEvent getEvent() {
		return event;
	}
}
