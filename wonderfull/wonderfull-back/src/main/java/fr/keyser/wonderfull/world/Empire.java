package fr.keyser.wonderfull.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.event.AffectProductionEvent;
import fr.keyser.wonderfull.world.event.MoveToProductionEvent;
import fr.keyser.wonderfull.world.event.RecycleEvent;
import fr.keyser.wonderfull.world.event.RecycleInProductionEvent;
import fr.keyser.wonderfull.world.game.PlayerScoreBoard;
import fr.keyser.wonderfull.world.game.ScoreAnalysis;

public class Empire {

	@JsonProperty
	private final List<BuildedCard> builded;

	private final List<InProductionCard> inProduction;

	/**
	 * Contains only {@link Token#KRYSTALIUM}, {@link Token#GENERAL} or
	 * {@link Token#BUSINESSMAN}
	 */
	private final Tokens onEmpire;

	private final int storedRaw;

	public final static Empire EMPTY = new Empire(Collections.emptyList(), Collections.emptyList(), Tokens.ZERO, 0);

	/**
	 * Create an {@link Empire} with a builded empire card
	 * 
	 * @param empireCard
	 * @return
	 */
	public static Empire with(MetaCard metaEmpire) {
		Tokens bonus = Optional.ofNullable(metaEmpire.getBonus()).orElse(Tokens.ZERO);
		return new Empire(Collections.singletonList(new BuildedCard(-1, metaEmpire)), Collections.emptyList(), bonus,
				0);
	}

	@JsonCreator
	public Empire(@JsonProperty("builded") List<BuildedCard> builded,
			@JsonProperty("inProduction") List<InProductionCard> inProduction,
			@JsonProperty("onEmpire") Tokens onEmpire, @JsonProperty("storedRaw") int storedRaw) {
		this.builded = Collections.unmodifiableList(builded);
		this.inProduction = Collections.unmodifiableList(inProduction);
		this.onEmpire = onEmpire;
		this.storedRaw = storedRaw;
	}

	public ScoreAnalysis scoreAnalysis() {
		return new ScoreAnalysis(score(), builded.size(),
				onEmpire.get(Token.BUSINESSMAN) + onEmpire.get(Token.GENERAL));
	}

	public Empire addTokens(Tokens tokens) {
		return new Empire(builded, inProduction, onEmpire.add(tokens), storedRaw);
	}

	public PlayerScoreBoard scoreBoard() {
		Tokens thisEmpire = cardsInEmpire().add(onEmpire);
		PlayerScoreBoard.Builder builder = new PlayerScoreBoard.Builder();
		for (BuildedCard card : builded)
			builder.add(card, thisEmpire);
		builder.add(Token.BUSINESSMAN, onEmpire.get(Token.BUSINESSMAN));
		builder.add(Token.GENERAL, onEmpire.get(Token.GENERAL));

		return builder.build();
	}

	public int score() {
		Tokens thisEmpire = cardsInEmpire().add(onEmpire);

		return builded.stream().mapToInt(b -> b.getScoring().resolve(thisEmpire)).sum() + onEmpire.get(Token.GENERAL)
				+ onEmpire.get(Token.BUSINESSMAN);
	}

	public Tokens producedAt(Token step) {

		if (Token.KRYSTALIUM == step) {
			Tokens b = Token.BUSINESSMAN.token(producedCount(Token.BUSINESSMAN));
			Tokens g = Token.GENERAL.token(producedCount(Token.GENERAL));
			Tokens k = Token.KRYSTALIUM.token(producedCount(Token.KRYSTALIUM));
			return b.add(g).add(k);
		}

		int produced = producedCount(step);
		if (produced == 0)
			return Tokens.ZERO;
		return step.token(produced);
	}

	private int producedCount(Token step) {
		Tokens thisEmpire = cardsInEmpire();
		int sum = builded.stream().mapToInt(b -> b.getProduce().resolve(step, thisEmpire)).sum();
		// gestion de la corruption
		return Math.max(0, sum);
	}

	public Tokens computeStats() {
		Tokens stats = Arrays.asList(Token.MATERIAL, Token.ENERGY, Token.SCIENCE, Token.GOLD, Token.DISCOVERY).stream()
				.map(this::producedAt).reduce(onEmpire, Tokens::add);

		Tokens b = Token.DELTABUSINESSMAN.token(producedCount(Token.BUSINESSMAN));
		Tokens g = Token.DELTAGENERAL.token(producedCount(Token.GENERAL));
		Tokens k = Token.DELTAKRYSTALIUM.token(producedCount(Token.KRYSTALIUM));

		return stats.add(b).add(g).add(k);
	}

	private Tokens cardsInEmpire() {
		return builded.stream().flatMap(b -> Optional.ofNullable(b.getType()).stream()).map(t -> t.token(1))
				.reduce(Tokens.ZERO, Tokens::add);
	}

	public RecycleEvent recycle(DraftedCard recycled) {
		return recycle(1, recycled);
	}

	public RecycleEvent recycle(int quantity) {
		return recycle(quantity, null);
	}

	private RecycleEvent recycle(int quantity, AbstractCard recycled) {
		int newStoredRaw = storedRaw + quantity;
		int add = newStoredRaw / 5;
		if (add > 0) {
			newStoredRaw = newStoredRaw - (add * 5);
		}
		return new RecycleEvent(quantity, newStoredRaw - storedRaw, add, recycled);
	}

	public Empire apply(RecycleEvent event) {
		return applyRecycle(event, builded, inProduction);
	}

	private Empire applyRecycle(RecycleEvent event, List<BuildedCard> newBuilded,
			List<InProductionCard> newProduction) {
		int newStoredRaw = storedRaw + event.getStoredRawDelta();
		Tokens newAvailable = onEmpire.add(Token.KRYSTALIUM.token(event.getKrystaliumDelta()));
		return new Empire(newBuilded, newProduction, newAvailable, newStoredRaw);
	}

	public MoveToProductionEvent moveToProduction(DraftedCard card) {
		return new MoveToProductionEvent(card.produce());
	}

	public Empire apply(MoveToProductionEvent event) {
		List<InProductionCard> newProduction = new ArrayList<>(inProduction.size() + 1);
		newProduction.addAll(inProduction);
		newProduction.add(event.getCard());
		return new Empire(builded, newProduction, onEmpire, storedRaw);
	}

	public Empire withProduction(List<InProductionCard> newProduction) {
		return new Empire(builded, newProduction, onEmpire, storedRaw);
	}

	public RecycleInProductionEvent recycleProduction(int targetId) {
		int index = AbstractCard.findIndex(inProduction, targetId);
		InProductionCard inProductionCard = inProduction.get(index);
		return new RecycleInProductionEvent(recycle(1, inProductionCard), index);
	}

	public Empire apply(RecycleInProductionEvent event) {
		List<InProductionCard> newInproduction = new ArrayList<>(inProduction);
		newInproduction.remove(event.getIndex());
		return applyRecycle(event, builded, newInproduction);

	}

	public AffectProductionEvent recycleToProduction(int targetId, DraftedCard recycled) {

		Token type = recycled.getRecycle();
		int index = AbstractCard.findIndex(inProduction, targetId);
		InProductionCard prod = inProduction.get(index);
		int slot = prod.firstEmptySlot(type);

		return affectToProduction(targetId, Map.of(slot, type), recycled);
	}

	public AffectProductionEvent affectToProduction(int targetId, Map<Integer, Token> affected) {
		return affectToProduction(targetId, affected, null);
	}

	private AffectProductionEvent affectToProduction(int targetId, Map<Integer, Token> affected, DraftedCard recycled) {
		int index = AbstractCard.findIndex(inProduction, targetId);
		InProductionCard prod = inProduction.get(index);
		InProductionCard newProd = prod.add(affected);
		Optional<BuildedCard> currentBuilded = newProd.builded();
		return new AffectProductionEvent(index, newProd, currentBuilded.orElse(null), affected, recycled);
	}

	public Empire apply(AffectProductionEvent event) {
		List<InProductionCard> newInproduction = new ArrayList<>(inProduction);
		BuildedCard added = event.getBuilded();
		int index = event.getIndex();
		InProductionCard prod = event.getInProduction();

		// consume the stored on empire resource
		Tokens toRemove = event.getAffected().values().stream().filter(Token::storedOnEmpire).map(Token::one)
				.reduce(Tokens.ZERO, Tokens::add);
		Tokens newOnEmpire = onEmpire.subtract(toRemove);

		if (added != null) {
			newInproduction.remove(index);

			List<BuildedCard> newBuilded = new ArrayList<>(builded.size() + 1);
			newBuilded.addAll(builded);
			newBuilded.add(added);

			// add the bonus
			Tokens newAvailable = newOnEmpire.add(prod.getBonus());

			return new Empire(newBuilded, newInproduction, newAvailable, storedRaw);

		} else {
			newInproduction.set(index, prod);
			return new Empire(builded, newInproduction, newOnEmpire, storedRaw);
		}
	}

	public int getStoredRaw() {
		return storedRaw;
	}

	public Tokens getOnEmpire() {
		return onEmpire;
	}

	public List<Integer> getTargetsForDirectRecycle(Token recycle) {
		return inProduction.stream().flatMap(i -> {
			if (i.require(recycle))
				return Stream.of(i.getId());
			else
				return Stream.empty();
		}).collect(Collectors.toList());
	}

	public List<InProductionCard> getInProduction() {
		return inProduction;
	}
}
