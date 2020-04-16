package fr.keyser.wonderfull.world.dto;

import java.util.List;

public class PlayerActionsDTO {

	private boolean supremacy;

	private boolean pass;

	private List<CardPossibleActions> cards;

	public boolean getSupremacy() {
		return supremacy;
	}

	public void setSupremacy(boolean supremacy) {
		this.supremacy = supremacy;
	}

	public boolean getPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public List<CardPossibleActions> getCards() {
		return cards;
	}

	public void setCards(List<CardPossibleActions> cards) {
		this.cards = cards;
	}

}
