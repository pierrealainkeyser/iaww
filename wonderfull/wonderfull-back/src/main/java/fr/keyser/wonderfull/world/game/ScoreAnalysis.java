package fr.keyser.wonderfull.world.game;

import static java.util.Comparator.comparingInt;

import java.util.Comparator;
import java.util.Objects;

public class ScoreAnalysis {

	public static final Comparator<ScoreAnalysis> COMPARATOR = comparingInt(ScoreAnalysis::getScore)
			.thenComparing(comparingInt(ScoreAnalysis::getCards)).thenComparing(comparingInt(ScoreAnalysis::getPeople));

	private final int cards;

	private final int people;

	private final int score;

	public ScoreAnalysis(int score, int cards, int people) {
		this.score = score;
		this.cards = cards;
		this.people = people;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScoreAnalysis other = (ScoreAnalysis) obj;
		return cards == other.cards && people == other.people && score == other.score;
	}

	public int getCards() {
		return cards;
	}

	public int getPeople() {
		return people;
	}

	public int getScore() {
		return score;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cards, people, score);
	}
}
