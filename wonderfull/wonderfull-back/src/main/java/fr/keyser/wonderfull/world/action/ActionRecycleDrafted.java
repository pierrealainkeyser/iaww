package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionRecycleDrafted extends ActionAbstractTarget implements EmpirePlayAction {

	@JsonCreator
	public ActionRecycleDrafted(@JsonProperty("targetId") int targetId) {
		super(targetId);
	}

}
