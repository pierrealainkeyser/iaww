package fr.keyser.wonderfull.world.game;

import java.util.EnumMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

import fr.keyser.wonderfull.world.BuildedCard;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.Tokens;

public class PlayerScoreBoard {

	public static class Builder {
		private final Map<Token, Integer> contributions = new EnumMap<>(Token.class);

		public Builder() {
			contributions.put(Token.GENERAL, 0);
			contributions.put(Token.BUSINESSMAN, 0);
			contributions.put(Token.MATERIAL, 0);
			contributions.put(Token.ENERGY, 0);
			contributions.put(Token.SCIENCE, 0);
			contributions.put(Token.GOLD, 0);
			contributions.put(Token.DISCOVERY, 0);
		}

		public Builder add(Token t, int delta) {
			contributions.compute(t, (type, score) -> {
				return score + delta;
			});
			return this;
		}

		public Builder add(BuildedCard card, Tokens inEmpire) {

			Token scoringType = card.getScoringType();
			if (contributions.containsKey(scoringType)) {
				contributions.compute(scoringType, (type, score) -> {
					int delta = card.getScoring().resolve(inEmpire);
					return score + delta;
				});
			}
			return this;
		}

		public PlayerScoreBoard build() {
			return new PlayerScoreBoard(contributions);
		}
	}

	private final Map<Token, Integer> contributions;

	public PlayerScoreBoard(Map<Token, Integer> contributions) {
		this.contributions = contributions;
	}

	@JsonValue
	public Map<Token, Integer> getContributions() {
		return contributions;
	}
}
