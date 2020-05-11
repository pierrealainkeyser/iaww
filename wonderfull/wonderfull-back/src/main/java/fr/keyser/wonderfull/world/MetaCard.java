package fr.keyser.wonderfull.world;

import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaCard {

	private final String name;

	private final Tokens bonus;

	private final Tokens cost;

	private final Production produce;

	private final Token recycle;

	private final Token type;

	private final int occurence;

	private final Value scoring;

	private String set;

	@JsonCreator
	public MetaCard(@JsonProperty("name") String name, @JsonProperty("occurence") int occurence,
			@JsonProperty("type") Token type, @JsonProperty("cost") Tokens cost, @JsonProperty("recycle") Token recycle,
			@JsonProperty("bonus") Tokens bonus, @JsonProperty("produce") Production produce,
			@JsonProperty("scoring") Value scoring) {
		this.name = name;
		this.occurence = occurence;
		this.type = type;
		this.cost = cost;
		this.recycle = recycle;
		this.bonus = bonus;
		this.produce = produce;
		this.scoring = scoring;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public Tokens getBonus() {
		return bonus;
	}

	public Tokens getCost() {
		return cost;
	}

	public Production getProduce() {
		return produce;
	}

	public Token getRecycle() {
		return recycle;
	}

	public String getName() {
		return name;
	}

	public int getOccurence() {
		return occurence;
	}

	public Token getType() {
		return type;
	}

	public Value getScoring() {
		return scoring;
	}

	public Token getScoringType() {
		if (scoring != null) {
			Iterator<Token> it = scoring.getEmpire().keySet().iterator();
			if (it.hasNext()) {
				return it.next();
			}
		}
		return type;

	}
}
