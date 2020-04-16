package fr.keyser.wonderfull.world;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CurrentProduction {

	private final Token step;

	private final Tokens available;

	public CurrentProduction(Token step, Tokens available) {
		this.step = step;
		this.available = available;
	}

	public CurrentProduction sync(Tokens empire) {
		return new CurrentProduction(step, step.token(available.get(step)).add(empire));
	}

	public Tokens computeSupremacy(boolean general) {
		if (Token.MATERIAL == step || Token.GOLD == step)
			return Token.BUSINESSMAN.token(1);
		else if (Token.ENERGY == step || Token.DISCOVERY == step)
			return Token.GENERAL.token(1);
		else {
			return (general ? Token.GENERAL : Token.BUSINESSMAN).token(1);
		}
	}

	public CurrentProduction affect(Tokens affected) {
		return new CurrentProduction(step, available.subtract(affected));
	}

	@JsonIgnore
	public int getRemaining() {
		return available.get(step);
	}

	public Tokens getAvailable() {
		return available;
	}

	public Token getStep() {
		return step;
	}

}
