package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionRecycleProduction extends ActionAbstractTarget implements EmpirePlayAction {

	@JsonCreator
	public ActionRecycleProduction(@JsonProperty("targetId") int targetId) {
		super(targetId);
	}

}
