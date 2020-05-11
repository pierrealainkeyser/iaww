package fr.keyser.wonderfull.world.dto;

import java.util.List;
import java.util.Map;

import fr.keyser.wonderfull.security.UserPrincipal;
import fr.keyser.wonderfull.world.DraftableCard;
import fr.keyser.wonderfull.world.DraftedCard;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.game.PlayerScoreBoard;

public class FullPlayerEmpireDTO {

	private Empire empire;

	private Integer score;

	private PlayerScoreBoard scoreBoard;

	private List<DraftedCard> drafteds;

	private List<DraftableCard> inHand;

	private List<DraftedCard> choice;

	private boolean done;

	private Map<Token, Integer> available;

	private UserPrincipal player;

	public Map<Token, Integer> getAvailable() {
		return available;
	}

	public List<DraftedCard> getDrafteds() {
		return drafteds;
	}

	public List<DraftedCard> getChoice() {
		return choice;
	}

	public void setChoice(List<DraftedCard> choice) {
		this.choice = choice;
	}

	public Empire getEmpire() {
		return empire;
	}

	public List<DraftableCard> getInHand() {
		return inHand;
	}

	public UserPrincipal getPlayer() {
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

	public void setPlayer(UserPrincipal player) {
		this.player = player;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public PlayerScoreBoard getScoreBoard() {
		return scoreBoard;
	}

	public void setScoreBoard(PlayerScoreBoard scoreBoard) {
		this.scoreBoard = scoreBoard;
	}

}
