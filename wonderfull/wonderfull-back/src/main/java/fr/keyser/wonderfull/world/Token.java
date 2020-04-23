package fr.keyser.wonderfull.world;

public enum Token {
	GENERAL, BUSINESSMAN, MATERIAL, ENERGY, SCIENCE, GOLD, DISCOVERY, KRYSTALIUM,

	// for stats and WOP
	DELTAGENERAL, DELTABUSINESSMAN, DELTAKRYSTALIUM,

	// for WOP
	MEMORIAL;

	public Tokens token(int count) {
		return new Tokens(this, count);
	}

	public Tokens one() {
		return token(1);
	}

	public boolean repleacable() {
		return MATERIAL == this || ENERGY == this || SCIENCE == this || GOLD == this || DISCOVERY == this;
	}

	public boolean storedOnEmpire() {
		return GENERAL == this || BUSINESSMAN == this || KRYSTALIUM == this;
	}
}
