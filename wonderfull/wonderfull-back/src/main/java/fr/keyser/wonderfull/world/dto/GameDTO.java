package fr.keyser.wonderfull.world.dto;

import java.util.List;

import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.event.EventAt;

public class GameDTO {

	private List<FullPlayerEmpireDTO> empires;

	private int myself;

	private int clock;

	private int turn;

	private String phase;

	private String step;

	private PlayerActionsDTO actions;

	private List<EventAt> events;

	private MetaCardDictionnary dictionnary;

	private Boolean wop;

	private boolean terminated;

	private Integer winner;

	public Integer getWinner() {
		return winner;
	}

	public void setWinner(Integer winner) {
		this.winner = winner;
	}

	public Boolean getWop() {
		return wop;
	}

	public void setWop(Boolean wop) {
		this.wop = wop;
	}

	public PlayerActionsDTO getActions() {
		return actions;
	}

	public int getClock() {
		return clock;
	}

	public MetaCardDictionnary getDictionnary() {
		return dictionnary;
	}

	public List<FullPlayerEmpireDTO> getEmpires() {
		return empires;
	}

	public List<EventAt> getEvents() {
		return events;
	}

	public int getMyself() {
		return myself;
	}

	public String getPhase() {
		return phase;
	}

	public String getStep() {
		return step;
	}

	public int getTurn() {
		return turn;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setActions(PlayerActionsDTO actions) {
		this.actions = actions;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public void setDictionnary(MetaCardDictionnary dictionnary) {
		this.dictionnary = dictionnary;
	}

	public void setEmpires(List<FullPlayerEmpireDTO> empires) {
		this.empires = empires;
	}

	public void setEvents(List<EventAt> events) {
		this.events = events;
	}

	public void setMyself(int myself) {
		this.myself = myself;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
}
