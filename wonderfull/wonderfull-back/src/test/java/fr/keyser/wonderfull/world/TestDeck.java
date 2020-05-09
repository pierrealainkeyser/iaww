package fr.keyser.wonderfull.world;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
			Assertions.assertThat(dc.getName()).isIn(cards);

	}
}
