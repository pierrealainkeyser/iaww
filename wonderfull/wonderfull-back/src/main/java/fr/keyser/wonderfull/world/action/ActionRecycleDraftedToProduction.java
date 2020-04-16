package fr.keyser.wonderfull.world.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionRecycleDraftedToProduction extends ActionAbstractTarget implements EmpirePlayAction {

	private final int productionId;

	@JsonCreator
	public ActionRecycleDraftedToProduction(@JsonProperty("targetId") int targetId,
			@JsonProperty("productionId") int productionId) {
		super(targetId);
		this.productionId = productionId;
	}

	public int getProductionId() {
		return productionId;
	}

}
