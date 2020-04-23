package fr.keyser.wonderfull.world.game;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
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
import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.action.ActionDispatch;
import fr.keyser.wonderfull.world.action.EmpireAction;
import fr.keyser.wonderfull.world.dto.FullPlayerEmpireDTO;
import fr.keyser.wonderfull.world.dto.GameDTO;
import fr.keyser.wonderfull.world.dto.PlayerActionsDTO;
import fr.keyser.wonderfull.world.event.EmpireEvent;

public class Game {

	@JsonProperty
	private final List<PlayerEmpire> empires;

	@JsonProperty
	private final Deck deck;

	private final GameConfiguration configuration;

	public static Game bootstrap(MetaCardDictionnaryLoader loader, GameConfiguration configuration) {

		MetaCardDictionnary dictionnary = loader.load(configuration.getDictionaries());
		Deck deck = Deck.builder(dictionnary).deck();

		List<PlayerEmpire> empires = configuration.getEmpires().stream()
				.map(c -> new PlayerEmpire(Empire.with(deck.resolve("empire/" + c.getEmpire()))))
				.collect(Collectors.toList());
		return new Game(configuration, deck, empires);
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
		newEmpires.set(index, player.dispatch(action, evt -> consumer.accept(index, evt)));
		return new Game(configuration, deck, newEmpires);
	}

	public GameDTO asDTO(int player, PlayersStatus status) {
		GameDTO dto = new GameDTO();
		dto.setMyself(player);
		if (status.isReady(player)) {
			dto.setActions(empires.get(player).asPlayerAction());
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

	public OptionalInt supremacyIndex(Token productionStep) {
		if (Token.KRYSTALIUM == productionStep)
			return OptionalInt.empty();

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
}
