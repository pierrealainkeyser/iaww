package fr.keyser.wonderfull.world.event;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.DraftedCard;

@JsonTypeName(DiscardToDigEvent.TYPE)
public class DiscardToDigEvent implements EmpireEvent {

	public static final String TYPE = "discard";

	private final List<DraftedCard> cards;

	private final List<Integer> indexes;

	private final List<DraftedCard> choice;

	@JsonCreator
	public DiscardToDigEvent(@JsonProperty("cards") List<DraftedCard> cards,
			@JsonProperty("indexes") List<Integer> indexes, @JsonProperty("choice") List<DraftedCard> choice) {
		this.cards = cards;
		this.indexes = indexes;
		this.choice = choice;
	}

	public List<DraftedCard> getChoice() {
		return choice;
	}

	public List<DraftedCard> getCards() {
		return cards;
	}

	public List<Integer> getIndexes() {
		return indexes;
	}
}
