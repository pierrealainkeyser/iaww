package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionSupremacy implements EmpirePlayAction {

	private final boolean general;

	@JsonCreator
	public ActionSupremacy(@JsonProperty("general") boolean general) {
		this.general = general;
	}

	public boolean isGeneral() {
		return general;
	}
}
