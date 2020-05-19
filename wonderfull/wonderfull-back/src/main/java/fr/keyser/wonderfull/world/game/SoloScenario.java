package fr.keyser.wonderfull.world.game;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SoloScenario {

	private final List<String> cards;

	private final Map<Integer, SoloRank> scores;

	public SoloScenario(int bronze, int silver, int gold, String... cards) {
		this.cards = Arrays.asList(cards);
		this.scores = new TreeMap<>();
		this.scores.put(0, SoloRank.FAILURE);
		this.scores.put(bronze, SoloRank.BRONZE);
		this.scores.put(silver, SoloRank.SILVER);
		this.scores.put(gold, SoloRank.GOLD);
	}

	public SoloRank computeRank(int score) {
		SoloRank rank = SoloRank.FAILURE;
		for (Entry<Integer, SoloRank> e : scores.entrySet()) {
			int toReach = e.getKey();
			if (score >= toReach)
				rank = e.getValue();
			else
				break;
		}

		return rank;
	}

	public List<String> getCards() {
		return cards;
	}
}
