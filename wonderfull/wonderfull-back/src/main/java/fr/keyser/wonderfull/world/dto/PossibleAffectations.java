package fr.keyser.wonderfull.world.dto;

import java.util.List;
import java.util.Map;

import fr.keyser.wonderfull.world.Token;

public class PossibleAffectations implements PossibleAction {

	private final Map<Integer, List<Token>> slots;

	public PossibleAffectations(Map<Integer, List<Token>> slots) {
		this.slots = slots;
	}

	@Override
	public String getAction() {
		return "affectation";
	}

	public Map<Integer, List<Token>> getSlots() {
		return slots;
	}

}
