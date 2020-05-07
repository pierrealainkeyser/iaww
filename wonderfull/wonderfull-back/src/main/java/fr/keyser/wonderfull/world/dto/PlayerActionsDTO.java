package fr.keyser.wonderfull.world.dto;

import java.util.List;

public class PlayerActionsDTO {

	private List<CardPossibleActions> cards;

	private boolean convert;

	private Boolean dig;

	private boolean pass;

	private boolean supremacy;

	private boolean undo;

	public List<CardPossibleActions> getCards() {
		return cards;
	}

	public boolean getPass() {
		return pass;
	}

	public boolean getSupremacy() {
		return supremacy;
	}

	public boolean isConvert() {
		return convert;
	}

	public Boolean isDig() {
		return dig;
	}

	public boolean isUndo() {
		return undo;
	}

	public void setCards(List<CardPossibleActions> cards) {
		this.cards = cards;
	}

	public void setConvert(boolean convert) {
		this.convert = convert;
	}

	public void setDig(Boolean dig) {
		this.dig = dig;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public void setSupremacy(boolean supremacy) {
		this.supremacy = supremacy;
	}

	public void setUndo(boolean undo) {
		this.undo = undo;
	}

}
