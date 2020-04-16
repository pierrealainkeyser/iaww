package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.DraftableCard;

@JsonTypeName(DraftEvent.TYPE)
public class DraftEvent implements EmpireEvent {

	public static final String TYPE = "draft";

	private final DraftableCard card;

	private final int index;

	@JsonCreator
	public DraftEvent(@JsonProperty("card") DraftableCard card, @JsonProperty("index") int index) {
		this.card = card;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public DraftableCard getCard() {
		return card;
	}
}
