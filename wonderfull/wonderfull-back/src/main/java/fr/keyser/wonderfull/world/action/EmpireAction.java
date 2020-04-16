package fr.keyser.wonderfull.world.action;

public interface EmpireAction {

	public default ActionDispatch dispatch(int empire) {
		return new ActionDispatch(empire, this);
	}
}
