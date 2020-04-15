package fr.keyser.wonderfull.world.dto;

import java.util.List;
import java.util.Map;

import fr.keyser.wonderfull.world.DraftableCard;
import fr.keyser.wonderfull.world.DraftedCard;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.Token;

public class FullPlayerEmpireDTO {

	private Empire empire;

	private Integer score;

	private List<DraftedCard> drafteds;

	private List<DraftableCard> inHand;

	private boolean done;

	private Map<Token, Integer> available;

	private String player;

	public Map<Token, Integer> getAvailable() {
		return available;
	}

	public List<DraftedCard> getDrafteds() {
		return drafteds;
	}

	public Empire getEmpire() {
		return empire;
	}

	public List<DraftableCard> getInHand() {
		return inHand;
	}

	public String getPlayer() {
		return player;
	}

	public Integer getScore() {
		return score;
	}

	public Map<Token, Integer> getStats() {
		return empire.computeStats().asMap();
	}

	public boolean isDone() {
		return done;
	}

	public void setAvailable(Map<Token, Integer> available) {
		this.available = available;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public void setDrafteds(List<DraftedCard> drafteds) {
		this.drafteds = drafteds;
	}

	public void setEmpire(Empire empire) {
		this.empire = empire;
	}

	public void setInHand(List<DraftableCard> inHand) {
		this.inHand = inHand;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
