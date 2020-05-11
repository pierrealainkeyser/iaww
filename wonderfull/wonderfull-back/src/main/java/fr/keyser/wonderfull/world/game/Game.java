package fr.keyser.wonderfull.world.game;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.Deck;
import fr.keyser.wonderfull.world.Deck.DraftablesCardsAndDeck;
import fr.keyser.wonderfull.world.DraftableCard;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.EmpireConfiguration;
import fr.keyser.wonderfull.world.GameConfiguration;
import fr.keyser.wonderfull.world.InProductionCard;
import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.action.ActionDiscardToDig;
import fr.keyser.wonderfull.world.action.ActionDispatch;
import fr.keyser.wonderfull.world.action.ActionUndo;
import fr.keyser.wonderfull.world.action.EmpireAction;
import fr.keyser.wonderfull.world.dto.FullPlayerEmpireDTO;
import fr.keyser.wonderfull.world.dto.GameDTO;
import fr.keyser.wonderfull.world.dto.PlayerActionsDTO;
import fr.keyser.wonderfull.world.event.EmpireEvent;

public class Game {

	public static final String A_BETTER_WORLD = "a_better_world";

	public static final String TO_THE_CENTER_OF_EARTH = "to_the_center_of_earth";

	private final List<PlayerEmpire> empires;

	@JsonProperty
	private final Deck deck;

	private final GameConfiguration configuration;

	public static List<String> cardsInScenario(String scenario) {

		if (TO_THE_CENTER_OF_EARTH.equals(scenario))
			return Arrays.asList("polar_base", "super_sonar", "center_of_the_earth", "mega_drill");
		else if (A_BETTER_WORLD.equals(scenario))
			return Arrays.asList("wind_turbines", "recycling_station", "universal_vaccine", "aquaculture");
		else if ("they_are_among_us".equals(scenario))
			return Arrays.asList("unknown_technology", "saucer_squadron", "lunar_base", "secret_laboratory");
		else if ("back_to_the_futur".equals(scenario))
			return Arrays.asList("research_center", "neuroscience", "time_travel");
		else if ("end_of_times".equals(scenario))
			return Arrays.asList("industrial_complex", "underground_city", "underwater_city", "secret_society");
		else if ("money_has_no_smell".equals(scenario))
			return Arrays.asList("financial_center", "propaganda_center", "national_monument");
		else
			return Collections.emptyList();
	}

	public static Game bootstrap(MetaCardDictionnaryLoader loader, GameConfiguration configuration) {

		MetaCardDictionnary dictionnary = loader.load(configuration.getDictionaries());
		Deck deck = Deck.builder(dictionnary).deck();
		Deck userDeck = deck;

		List<PlayerEmpire> empires = configuration.getEmpires().stream()
				.map(c -> new PlayerEmpire(Empire.with(deck.resolve("empire/" + c.getEmpire()))))
				.collect(Collectors.toList());

		if (empires.size() == 1) {

			List<String> cards = cardsInScenario(configuration.getStartingEmpire());
			if (!cards.isEmpty()) {
				userDeck = userDeck.prepareForScenario(cards);
				DraftablesCardsAndDeck next = userDeck.next(cards.size());
				List<InProductionCard> inProduction = next.getCards().stream().map(s -> s.draft().produce())
						.collect(Collectors.toList());
				userDeck = next.getDeck();

				Empire empire = empires.get(0).getEmpire().withProduction(inProduction);
				empires = Arrays.asList(new PlayerEmpire(empire));
			}

		}

		return new Game(configuration, userDeck, empires);
	}

	@JsonCreator
	public Game(@JsonProperty("configuration") GameConfiguration configuration, @JsonProperty("deck") Deck deck,
			@JsonProperty("empires") List<PlayerEmpire> empires) {
		this.configuration = configuration;
		this.deck = deck;
		this.empires = empires;
	}

	public Game dispatch(ActionDispatch dispatch, BiConsumer<Integer, EmpireEvent> consumer) {

		int index = dispatch.getIndex();

		EmpireAction action = dispatch.getAction();
		List<PlayerEmpire> newEmpires = new ArrayList<>(empires);
		PlayerEmpire player = newEmpires.get(index);
		Consumer<EmpireEvent> forward = evt -> consumer.accept(index, evt);

		Deck deck = this.deck;

		PlayerEmpire dispatched = null;
		if (action instanceof ActionDiscardToDig) {
			DraftablesCardsAndDeck next = deck.next(5);
			deck = next.getDeck();
			dispatched = player.discardToDig((ActionDiscardToDig) action, next.getCards(), forward);
		} else {

			// can't undo when alone
			if (action instanceof ActionUndo && empires.size() == 1) {
				throw new IllegalActionException("undo");
			}

			dispatched = player.dispatch(action, forward);
		}
		newEmpires.set(index, dispatched);

		return new Game(configuration, deck, newEmpires);
	}

	public GameDTO asDTO(int player, PlayersStatus status) {
		GameDTO dto = new GameDTO();
		dto.setMyself(player);
		if (status.isReady(player)) {
			dto.setActions(empires.get(player).asPlayerAction(1 == empires.size()));
		} else if (status.isWaitingSupremacy(player)) {
			PlayerActionsDTO actions = new PlayerActionsDTO();
			actions.setSupremacy(true);
			dto.setActions(actions);
		}

		int size = empires.size();
		List<FullPlayerEmpireDTO> emp = new ArrayList<>(size);
		for (int i = 0; i < size; ++i) {
			FullPlayerEmpireDTO eDto = empires.get(i).asDTO(status.isDone(i), player != i);
			EmpireConfiguration ec = configuration.getEmpires().get(i);
			eDto.setPlayer(ec.getUser());
			emp.add(eDto);
		}
		dto.setEmpires(emp);

		return dto;
	}

	public Game nextTurn() {
		int count = empires.size();
		Deck newDeck = deck;
		int perEmpire = count == 2 ? 10 : 7;
		List<PlayerEmpire> newEmpires = new ArrayList<>(empires);
		for (int i = 0; i < count; ++i) {
			PlayerEmpire player = newEmpires.get(i);

			DraftablesCardsAndDeck next = newDeck.next(perEmpire);
			newEmpires.set(i, player.draft(next.getCards()));

			newDeck = next.getDeck();
		}

		return new Game(configuration, newDeck, newEmpires);
	}

	public Game startPlanning() {
		return new Game(configuration, deck, mapEmpire(PlayerEmpire::draftToPlanning));
	}

	public Game startSinglePlayerPlanning() {
		PlayerEmpire playerEmpire = empires.get(0);
		DraftablesCardsAndDeck next = deck.next(5);
		return new Game(configuration, next.getDeck(),
				singletonList(playerEmpire.singlePlayerPlanning(next.getCards())));
	}

	public OptionalInt supremacyIndex(Token productionStep) {
		if (Token.KRYSTALIUM == productionStep)
			return OptionalInt.empty();

		if (1 == empires.size()) {
			int produced = empires.get(0).getProduction().getAvailable().get(productionStep);
			return produced >= 5 ? OptionalInt.of(0) : OptionalInt.empty();
		}

		int[] production = empires.stream().mapToInt(pe -> pe.getProduction().getAvailable().get(productionStep))
				.toArray();
		int max = IntStream.of(production).reduce(0, Math::max);

		long count = IntStream.of(production).filter(p -> p == max).count();
		if (count == 1) {
			for (int i = 0; i < production.length; ++i) {
				if (production[i] == max)
					return OptionalInt.of(i);
			}
		}
		return OptionalInt.empty();
	}

	public Game nextDraftStep(int turn) {

		List<PlayerEmpire> newEmpires = new ArrayList<>(empires);
		List<List<DraftableCard>> inHands = newEmpires.stream().map(pe -> pe.getDraft().getInHand())
				.collect(Collectors.toList());
		int count = empires.size();
		int direction = turn % 2 == 0 ? 1 : -1;

		for (int i = 0; i < count; ++i) {
			PlayerEmpire player = newEmpires.get(i);
			int next = (i + count + direction) % count;
			PlayerEmpire newPlayer = player.draft(inHands.get(next));
			newEmpires.set(i, newPlayer);
		}

		return new Game(configuration, deck, newEmpires);
	}

	private List<PlayerEmpire> mapEmpire(UnaryOperator<PlayerEmpire> op) {
		return empires.stream().map(op).collect(Collectors.toList());
	}

	public Game startProductionStep(Token productionStep) {
		return new Game(configuration, deck, mapEmpire(e -> e.startProductionStep(productionStep)));
	}

	public Game endProductionStep(BiConsumer<Integer, EmpireEvent> consumer) {

		List<PlayerEmpire> newEmpires = new ArrayList<>(empires);
		for (int i = 0; i < newEmpires.size(); ++i) {
			PlayerEmpire e = newEmpires.get(i);
			int index = i;
			newEmpires.set(i, e.endProductionStep(evt -> consumer.accept(index, evt)));
		}

		return new Game(configuration, deck, newEmpires);
	}

	public Game reloadDictionnary(MetaCardDictionnaryLoader loader) {
		return new Game(configuration, deck.withDictionnary(loader.load(configuration.getDictionaries())), empires);
	}

	@JsonIgnore
	public int getPlayerCount() {
		return configuration.getEmpires().size();
	}

	public GameConfiguration getConfiguration() {
		return configuration;
	}

	public Integer winner() {

		Map<Integer, ScoreAnalysis> scores = new TreeMap<>();
		for (int i = 0; i < empires.size(); ++i) {
			ScoreAnalysis scoreAnalysis = empires.get(i).resolveEmpire().scoreAnalysis();
			scores.put(i, scoreAnalysis);
		}

		ScoreAnalysis max = scores.values().stream().max(ScoreAnalysis.COMPARATOR).get();

		Integer winner = null;
		for (Entry<Integer, ScoreAnalysis> e : scores.entrySet()) {
			ScoreAnalysis s = e.getValue();
			if (max.equals(s)) {
				// all ready a winner
				if (winner != null)
					return null;
				else
					winner = e.getKey();
			}
		}

		return winner;
	}

	public List<PlayerEmpire> getEmpires() {
		return empires;
	}
	
	
	public List<PlayerScoreBoard> scoreBoards() {
		return empires.stream().map(pe -> pe.resolveEmpire().scoreBoard()).collect(Collectors.toList());
	}
}
