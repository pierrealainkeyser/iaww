package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionDraft extends ActionAbstractTarget implements EmpirePlayAction {

	@JsonCreator
	public ActionDraft(@JsonProperty("targetId") int targetId) {
		super(targetId);
	}

}
