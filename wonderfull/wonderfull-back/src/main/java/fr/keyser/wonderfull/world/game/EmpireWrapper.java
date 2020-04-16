package fr.keyser.wonderfull.world.game;

import fr.keyser.wonderfull.world.Empire;

/**
 * A wrapper is the main action entry point
 * 
 * @author pakeyser
 *
 */
public abstract class EmpireWrapper {

	protected final Empire empire;

	public EmpireWrapper(Empire empire) {
		this.empire = empire;
	}

	public Empire getEmpire() {
		return empire;
	}

}
