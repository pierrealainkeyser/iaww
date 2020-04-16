package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.Tokens;

@JsonTypeName(SupremacyEvent.TYPE)
public class SupremacyEvent implements EmpireEvent {

	public static final String TYPE = "supremacy";

	private final Tokens gain;

	@JsonCreator
	public SupremacyEvent(@JsonProperty("gain") Tokens gain) {
		this.gain = gain;
	}

	public Tokens getGain() {
		return gain;
	}
}
