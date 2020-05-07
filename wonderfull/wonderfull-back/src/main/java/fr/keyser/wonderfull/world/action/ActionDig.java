package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionDig extends ActionAbstractTarget implements EmpirePlayAction {

	@JsonCreator
	public ActionDig(@JsonProperty("targetId") int targetId) {
		super(targetId);
	}

}
