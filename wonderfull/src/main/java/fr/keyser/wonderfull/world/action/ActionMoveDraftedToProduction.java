package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionMoveDraftedToProduction extends ActionAbstractTarget implements EmpirePlayAction {

	@JsonCreator
	public ActionMoveDraftedToProduction(@JsonProperty("targetId") int targetId) {
		super(targetId);
	}

}
