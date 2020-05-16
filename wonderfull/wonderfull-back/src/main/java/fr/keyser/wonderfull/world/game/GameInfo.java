package fr.keyser.wonderfull.world.game;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.action.ActionDispatch;
import fr.keyser.wonderfull.world.event.EventAt;

public class GameInfo {

	private final Game game;

	private final int turn;

	@JsonProperty
	private final int drafteds;

	private final int clock;

	private final List<Integer> clocks;

	private final List<EventAt> events;

	private final List<EventAt> privateEvents;

	public GameInfo(Game game) {
		this(game, 0, 0, 0, null, emptyList(), emptyList());
	}

	@JsonCreator
	public GameInfo(@JsonProperty("game") Game game, @JsonProperty("turn") int turn,
			@JsonProperty("drafteds") int drafteds, @JsonProperty("clock") int clock,
			@JsonProperty("clocks") List<Integer> clocks, @JsonProperty("events") List<EventAt> events,
			@JsonProperty("privateEvents") List<EventAt> privateEvents) {
		this.game = game;
		this.turn = turn;
		this.drafteds = drafteds;
		this.clock = clock;
		this.clocks = unmodifiableList(clocks == null ? createClocks(clock, game) : clocks);
		this.events = unmodifiableList(events != null ? events : emptyList());
		this.privateEvents = unmodifiableList(privateEvents != null ? privateEvents : emptyList());
	}

	private static List<Integer> createClocks(int clock, Game game) {
		return IntStream.range(0, game.getPlayerCount()).map(i -> clock).mapToObj(Integer::valueOf).collect(toList());
	}

	public GameInfo reloadDictionnary(MetaCardDictionnaryLoader loader) {
		return new GameInfo(game.reloadDictionnary(loader), turn, drafteds, clock, clocks, events, privateEvents);
	}

	public int getClock() {
		return clock;
	}

	public List<EventAt> getEvents() {
		return events;
	}

	public Game getGame() {
		return game;
	}

	public int getTurn() {
		return turn;
	}

	@JsonIgnore
	public boolean isDoneDrafting() {
		return drafteds >= 7;
	}

	@JsonIgnore
	public boolean isDonePlaying() {
		return turn >= 4;
	}

	public Integer winner() {
		return game.winner();
	}

	public List<PlayerScoreBoard> scoreBoards() {
		return game.scoreBoards();
	}

	public GameInfo nextDraft() {
		return new GameInfo(game.nextDraftStep(turn), turn, drafteds + 1, clock, clocks, events, privateEvents)
				.publishEvents();
	}

	public GameInfo publishEvents() {
		List<EventAt> newEvents = new ArrayList<>(events.size() + privateEvents.size());
		newEvents.addAll(events);
		newEvents.addAll(privateEvents);

		return new GameInfo(game, turn, drafteds, clock, clocks, newEvents, Collections.emptyList());
	}

	public GameInfo nextTurn() {
		return new GameInfo(game.nextTurn(), turn + 1, 0, clock, clocks, events, privateEvents);
	}

	public GameInfo nextTurnSinglePlayer() {
		return new GameInfo(game, turn + 1, 0, clock, clocks, events, privateEvents);
	}

	public boolean mayAutoPassSinglePlayer() {
		EmpirePlanningWrapper planning = game.getEmpires().get(0).getPlanning();
		return planning.getDrafteds().isEmpty() && planning.getChoice().isEmpty();
	}

	@Override
	public String toString() {
		return String.format("[turn=%s, drafteds=%s, clock=%s]]", turn, drafteds, clock);
	}

	public GameInfo dispatch(ActionDispatch dispatch) {
		int nextClock = clock + 1;
		List<EventAt> newEvents = new ArrayList<>(events);
		Game newGame = dispatchAction(dispatch, nextClock, newEvents);
		return new GameInfo(newGame, turn, drafteds, nextClock, newClocks(dispatch, nextClock), newEvents,
				privateEvents);
	}

	public GameInfo privateDispatch(ActionDispatch dispatch) {
		int nextClock = clock + 1;
		List<EventAt> newEvents = new ArrayList<>(privateEvents);
		Game newGame = dispatchAction(dispatch, nextClock, newEvents);
		return new GameInfo(newGame, turn, drafteds, nextClock, newClocks(dispatch, nextClock), events, newEvents);
	}

	private List<Integer> newClocks(ActionDispatch dispatch, int nextClock) {
		List<Integer> clocks = new ArrayList<>(this.clocks);
		clocks.set(dispatch.getIndex(), nextClock);
		return clocks;
	}

	private Game dispatchAction(ActionDispatch dispatch, int nextClock, List<EventAt> newEvents) {
		return game.dispatch(dispatch, (player, event) -> newEvents.add(new EventAt(nextClock, player, event)));
	}

	public GameInfo startProductionStep(Token step) {
		int nextClock = clock + 1;
		return new GameInfo(game.startProductionStep(step), turn, drafteds, nextClock, createClocks(nextClock, game),
				events, privateEvents);
	}

	public GameInfo startPlanning() {
		int nextClock = clock + 1;
		return new GameInfo(game.startPlanning(), turn, drafteds, nextClock, createClocks(nextClock, game), events,
				privateEvents);
	}

	public GameInfo startSinglePlayerPlanning() {
		int nextClock = clock + 1;
		return new GameInfo(game.startSinglePlayerPlanning(), turn, drafteds, clock + 1, createClocks(nextClock, game),
				events, privateEvents);
	}

	public GameInfo endProductionStep() {
		int nextClock = clock + 1;
		List<EventAt> newEvents = new ArrayList<>(events);
		Game newGame = game.endProductionStep((player, event) -> newEvents.add(new EventAt(nextClock, player, event)));
		return new GameInfo(newGame, turn, drafteds, nextClock, createClocks(nextClock, game), newEvents,
				privateEvents);
	}

	public SoloRank soloRank() {
		return game.soloRank();
	}

	public int clockFor(int player) {
		return getClocks().get(player);
	}

	public List<Integer> getClocks() {
		return clocks;
	}
}
