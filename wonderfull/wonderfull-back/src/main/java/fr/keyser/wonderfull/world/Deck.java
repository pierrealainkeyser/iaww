package fr.keyser.wonderfull.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Deck {

	private final MetaCardDictionnary dictionnary;

	@JsonProperty
	private final List<ProtoCard> protocards;

	@JsonProperty
	private final int nextIndex;

	public static class Builder {
		private final MetaCardDictionnary dictionnary;

		private List<ProtoCard> protocards;

		private boolean shuffle = true;

		private Builder(MetaCardDictionnary dictionnary) {
			this.dictionnary = dictionnary;
		}

		public Builder with(List<ProtoCard> protocards) {
			this.protocards = protocards;
			this.shuffle = false;
			return this;
		}

		public Deck deck() {
			if (protocards == null) {
				AtomicInteger idProvider = new AtomicInteger(0);
				protocards = dictionnary.getCards().stream()
						.flatMap(m -> IntStream.range(0, m.getOccurence())
								.mapToObj(i -> new ProtoCard(idProvider.getAndIncrement(), m.getName())))
						.collect(Collectors.toCollection(ArrayList::new));
			}

			List<ProtoCard> used = new ArrayList<>(protocards);
			if (shuffle)
				Collections.shuffle(used);

			return new Deck(dictionnary, used, 0);
		}
	}

	public static Builder builder(MetaCardDictionnary dictionnary) {
		return new Builder(dictionnary);
	}

	@JsonCreator
	public Deck(@JsonProperty("protocards") List<ProtoCard> protocards, @JsonProperty("nextIndex") int nextIndex) {
		this(null, protocards, nextIndex);
	}

	public Deck(MetaCardDictionnary dictionnary, List<ProtoCard> protocards, int nextIndex) {
		this.dictionnary = dictionnary;
		this.protocards = Collections.unmodifiableList(protocards);
		this.nextIndex = nextIndex;
	}

	public Deck withDictionnary(MetaCardDictionnary dictionnary) {
		return new Deck(dictionnary, protocards, nextIndex);
	}

	public static class DraftablesCardsAndDeck {
		private final Deck deck;

		private final List<DraftableCard> cards;

		private DraftablesCardsAndDeck(Deck deck, List<DraftableCard> cards) {
			this.deck = deck;
			this.cards = Collections.unmodifiableList(cards);
		}

		public Deck getDeck() {
			return deck;
		}

		public List<DraftableCard> getCards() {
			return cards;
		}

	}

	public Deck prepareForScenario(List<String> cards) {
		List<String> remainnings = new ArrayList<>(cards);
		List<ProtoCard> newProtos = new ArrayList<>(protocards);

		List<ProtoCard> founds = new ArrayList<>();

		Iterator<ProtoCard> it = newProtos.iterator();
		while (it.hasNext() && !remainnings.isEmpty()) {
			ProtoCard next = it.next();
			String name = next.getName();
			int index = remainnings.indexOf(name);
			if (index >= 0) {
				remainnings.remove(index);
				it.remove();
				founds.add(next);
			}
		}

		newProtos.addAll(0, founds);

		return new Deck(dictionnary, newProtos, 0);

	}

	public DraftablesCardsAndDeck next(int qty) {

		int toIndex = nextIndex + qty;
		List<ProtoCard> ps = protocards.subList(nextIndex, toIndex);

		List<DraftableCard> cards = ps.stream().map(p -> new DraftableCard(p.getId(), dictionnary.resolve(p.getName())))
				.collect(Collectors.toList());

		return new DraftablesCardsAndDeck(new Deck(dictionnary, protocards, toIndex), cards);
	}

	public MetaCard resolve(String name) {
		return dictionnary.resolve(name);
	}

}
