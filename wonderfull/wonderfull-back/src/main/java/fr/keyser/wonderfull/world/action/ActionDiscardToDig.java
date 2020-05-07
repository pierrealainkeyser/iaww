package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionDiscardToDig implements EmpirePlayAction {

	private final int[] targetsIds;

	@JsonCreator
	public ActionDiscardToDig(@JsonProperty("targetsIds") int[] targetsIds) {
		this.targetsIds = targetsIds;
	}

	public int[] getTargetsIds() {
		return targetsIds;
	}
}
