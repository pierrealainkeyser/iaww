package fr.keyser.wonderfull.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;

public class MetaCardDictionnary {

	private final Map<String, MetaCard> cards;

	@JsonCreator
	public MetaCardDictionnary(@JsonUnwrapped List<MetaCard> cards) {
		this(Collections
				.unmodifiableMap(cards.stream().collect(Collectors.toMap(MetaCard::getName, Function.identity()))));
	}

	private MetaCardDictionnary(Map<String, MetaCard> cards) {
		this.cards = cards;
	}

	public MetaCardDictionnary merge(MetaCardDictionnary other) {
		Map<String, MetaCard> newCards = new TreeMap<>();
		newCards.putAll(cards);
		newCards.putAll(other.cards);
		return new MetaCardDictionnary(newCards);
	}

	public MetaCard resolve(String name) {
		MetaCard out = cards.get(name);
		if (out == null)
			throw new NoSuchMetaCardException(name);
		return out;
	}

	@JsonValue
	public List<MetaCard> getCards() {
		return new ArrayList<>(cards.values());
	}

}
