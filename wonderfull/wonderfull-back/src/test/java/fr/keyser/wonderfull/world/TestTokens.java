package fr.keyser.wonderfull.world;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.Tokens;

public class TestTokens {

	@Test
	void json() throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		Tokens t = Token.ENERGY.token(4).add(Token.SCIENCE.token(1));
		String json = om.writeValueAsString(t);

		Tokens read = om.readerFor(Tokens.class).readValue(json);
		Assertions.assertThat(read).isEqualTo(t);
		Assertions.assertThat(json).contains("ENERGY*4").contains(", ").contains("SCIENCE").doesNotContain("SCIENCE*1");
	}

	@Test
	void math() {
		Tokens oneGold = Token.GOLD.token(1);
		Tokens twoGold = oneGold.add(oneGold);

		Assertions.assertThat(twoGold).isEqualTo(Token.GOLD.token(2));

		Tokens minus = twoGold.subtract(oneGold);

		Assertions.assertThat(minus).isEqualTo(oneGold);

		Tokens floored = minus.subtract(twoGold).floor();
		Assertions.assertThat(floored.isEmpty()).isTrue();

		Assertions.assertThat(twoGold.min(oneGold)).isEqualTo(oneGold);

	}

}
