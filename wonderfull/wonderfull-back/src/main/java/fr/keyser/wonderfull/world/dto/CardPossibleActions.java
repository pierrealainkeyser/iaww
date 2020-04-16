package fr.keyser.wonderfull.world.dto;

import java.util.List;

public class CardPossibleActions {

	private final int targetId;

	private final List<PossibleAction> actions;

	public CardPossibleActions(int targetId, List<PossibleAction> actions) {
		this.targetId = targetId;
		this.actions = actions;
	}

	public int getTargetId() {
		return targetId;
	}

	public List<PossibleAction> getActions() {
		return actions;
	}

}
