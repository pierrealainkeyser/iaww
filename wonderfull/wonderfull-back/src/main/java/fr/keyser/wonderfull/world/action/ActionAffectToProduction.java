package fr.keyser.wonderfull.world.action;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.Token;

public class ActionAffectToProduction extends ActionAbstractTarget implements EmpirePlayAction {

	private final Map<Integer, Token> slots;

	@JsonCreator
	public ActionAffectToProduction(@JsonProperty("targetId") int targetId,
			@JsonProperty("slots") Map<Integer, Token> slots) {
		super(targetId);
		this.slots = slots;
	}

	public Map<Integer, Token> getSlots() {
		return slots;
	}
}
