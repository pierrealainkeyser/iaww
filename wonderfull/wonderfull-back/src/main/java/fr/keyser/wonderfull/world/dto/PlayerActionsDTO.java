package fr.keyser.wonderfull.world.dto;

import java.util.List;

public class PlayerActionsDTO {

	private boolean supremacy;

	private boolean pass;

	private boolean convert;

	private List<CardPossibleActions> cards;

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

	public void setCards(List<CardPossibleActions> cards) {
		this.cards = cards;
	}

	public void setConvert(boolean convert) {
		this.convert = convert;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public void setSupremacy(boolean supremacy) {
		this.supremacy = supremacy;
	}

}
