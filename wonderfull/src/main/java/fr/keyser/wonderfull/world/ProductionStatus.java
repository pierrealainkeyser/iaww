package fr.keyser.wonderfull.world;

public enum ProductionStatus {
	EMPTY, OK, KRYSTALIUM;

	public boolean ok() {
		return OK == this || KRYSTALIUM == this;
	}
}