package fr.keyser.wonderfull.world.action;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.Tokens;

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

	@JsonIgnore
	public Tokens getConsumed() {
		Tokens tokens = Tokens.ZERO;
		for (Token token : slots.values())
			tokens = tokens.add(token.token(1));
		return tokens;
	}

}
