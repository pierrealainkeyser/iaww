package fr.keyser.wonderfull.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.keyser.wonderfull.world.Deck.DraftablesCardsAndDeck;

public class TestDeck {
	@Test
	void nominal() {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("core");
		Deck deck = Deck.builder(dictionnary).deck();

		List<String> cards = Arrays.asList("polar_base", "super_sonar", "center_of_the_earth", "mega_drill");

		deck = deck.prepareForScenario(cards);
		List<DraftableCard> drafteds = deck.next(cards.size()).getCards();

		for (DraftableCard dc : drafteds)
			assertThat(dc.getName()).isIn(cards);

	}

	@Test
	void ascension() {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load(Arrays.asList("core", "asc"));
		Deck deck = Deck.builder(dictionnary).deck();

		deck = deck.prepareForAscension(7, 3);

		for (int i = 0; i < 2; ++i) {
			DraftablesCardsAndDeck next = deck.next(10);
			deck = next.getDeck();
			List<DraftableCard> cards = next.getCards();

			assertThat(cards.get(7).getSet()).isEqualTo("asc");
			assertThat(cards.get(8).getSet()).isEqualTo("asc");
			assertThat(cards.get(9).getSet()).isEqualTo("asc");
		}

	}
}
