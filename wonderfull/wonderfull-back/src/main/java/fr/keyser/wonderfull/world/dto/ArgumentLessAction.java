package fr.keyser.wonderfull.world.dto;

public class ArgumentLessAction implements PossibleAction {

	public final static PossibleAction DIG = new ArgumentLessAction("dig");
	
	public final static PossibleAction DRAFT = new ArgumentLessAction("draft");

	public final static PossibleAction RECYLE_DRAFT = new ArgumentLessAction("recycleDraft");

	public final static PossibleAction RECYLE_PRODUCTION = new ArgumentLessAction("recycleProduction");

	public final static PossibleAction MOVE_TO_PRODUCTION = new ArgumentLessAction("moveToProduction");

	private final String action;

	@Override
	public String getAction() {
		return action;
	}

	public ArgumentLessAction(String action) {
		this.action = action;
	}

}
