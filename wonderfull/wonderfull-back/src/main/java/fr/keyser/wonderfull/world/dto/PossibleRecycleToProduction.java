package fr.keyser.wonderfull.world.dto;

import java.util.List;

public class PossibleRecycleToProduction implements PossibleAction {

	private final List<Integer> targets;

	public PossibleRecycleToProduction(List<Integer> targets) {
		this.targets = targets;
	}

	@Override
	public String getAction() {
		return "recycleToProduction";
	}

	public List<Integer> getTargets() {
		return targets;
	}

}
