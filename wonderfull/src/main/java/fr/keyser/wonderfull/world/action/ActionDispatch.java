package fr.keyser.wonderfull.world.action;

public class ActionDispatch {

	private final int index;

	private final EmpireAction action;

	public ActionDispatch(int index, EmpireAction action) {
		this.index = index;
		this.action = action;
	}

	public int getIndex() {
		return index;
	}

	public EmpireAction getAction() {
		return action;
	}

}
