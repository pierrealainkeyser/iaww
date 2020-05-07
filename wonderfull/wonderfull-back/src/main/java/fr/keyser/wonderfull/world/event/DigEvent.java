package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.DraftedCard;

@JsonTypeName(DigEvent.TYPE)
public class DigEvent implements EmpireEvent {

	public static final String TYPE = "dig";

	private final DraftedCard card;

	private final int index;

	@JsonCreator
	public DigEvent(@JsonProperty("card") DraftedCard card, @JsonProperty("index") int index) {
		this.card = card;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public DraftedCard getCard() {
		return card;
	}
}
