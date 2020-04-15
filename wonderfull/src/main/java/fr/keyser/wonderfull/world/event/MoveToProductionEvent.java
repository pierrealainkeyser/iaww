package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.InProductionCard;

@JsonTypeName(MoveToProductionEvent.TYPE)
public class MoveToProductionEvent implements EmpireEvent {

	public static final String TYPE = "move";

	private final InProductionCard card;

	@JsonCreator
	public MoveToProductionEvent(@JsonProperty("card") InProductionCard card) {
		this.card = card;
	}

	public InProductionCard getCard() {
		return card;
	}
}
