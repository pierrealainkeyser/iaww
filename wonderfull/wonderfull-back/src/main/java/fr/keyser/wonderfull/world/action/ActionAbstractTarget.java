package fr.keyser.wonderfull.world.action;

public abstract class ActionAbstractTarget  {

	private final int targetId;

	public ActionAbstractTarget(int targetId) {
		this.targetId = targetId;
	}

	public int getTargetId() {
		return targetId;
	}

}
