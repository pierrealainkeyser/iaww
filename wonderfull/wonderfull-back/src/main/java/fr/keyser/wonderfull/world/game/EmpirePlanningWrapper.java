package fr.keyser.wonderfull.world.game;

import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.CurrentPlanning;
import fr.keyser.wonderfull.world.DraftedCard;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.Tokens;
import fr.keyser.wonderfull.world.action.ActionAffectToProduction;
import fr.keyser.wonderfull.world.action.ActionDig;
import fr.keyser.wonderfull.world.action.ActionDiscardToDig;
import fr.keyser.wonderfull.world.action.ActionMoveDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleDrafted;
import fr.keyser.wonderfull.world.action.ActionRecycleDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleProduction;
import fr.keyser.wonderfull.world.event.AffectProductionEvent;
import fr.keyser.wonderfull.world.event.DigEvent;
import fr.keyser.wonderfull.world.event.DiscardToDigEvent;
import fr.keyser.wonderfull.world.event.EmpireEvent;
import fr.keyser.wonderfull.world.event.MoveToProductionEvent;
import fr.keyser.wonderfull.world.event.RecycleEvent;
import fr.keyser.wonderfull.world.event.RecycleInProductionEvent;

public class EmpirePlanningWrapper extends EmpireWrapper {

	private final CurrentPlanning planning;

	@JsonCreator
	public EmpirePlanningWrapper(@JsonProperty("empire") Empire empire,
			@JsonProperty("drafteds") List<DraftedCard> drafteds, @JsonProperty("choice") List<DraftedCard> choice) {
		this(empire, new CurrentPlanning(drafteds, choice));
	}

	private EmpirePlanningWrapper(Empire empire, CurrentPlanning planning) {
		super(empire);
		this.planning = planning;
	}

	/**
	 * Recyle a card in the production line
	 * 
	 * @param action
	 * @param consumer
	 * @return
	 */
	public EmpirePlanningWrapper recycleProduction(ActionRecycleProduction action, Consumer<EmpireEvent> consumer) {

		RecycleInProductionEvent event = empire.recycleProduction(action.getTargetId());

		consumer.accept(event);

		return recyleProduction(event);
	}

	public EmpirePlanningWrapper dig(ActionDig dig, Consumer<EmpireEvent> consumer) {
		DigEvent event = planning.dig(dig.getTargetId());
		consumer.accept(event);
		return new EmpirePlanningWrapper(empire, planning.dig(event));
	}

	public EmpirePlanningWrapper discardToDig(ActionDiscardToDig action, List<DraftedCard> cards,
			Consumer<EmpireEvent> consumer) {
		DiscardToDigEvent event = planning.discardToDig(action, cards);
		consumer.accept(event);
		return new EmpirePlanningWrapper(empire, planning.discard(event, cards));
	}

	/**
	 * Affect some currently produced resources to a card in production
	 * 
	 * @param consumer
	 * @param action
	 * @return
	 */
	public EmpirePlanningWrapper affectToProduction(ActionAffectToProduction action, Consumer<EmpireEvent> consumer) {

		AffectProductionEvent event = empire.affectToProduction(action.getTargetId(), action.getSlots());

		Tokens consumed = event.getConsumed();
		Tokens available = empire.getOnEmpire();
		// check affectation
		Tokens remaining = available.subtract(consumed);
		if (remaining.entrySet().stream().anyMatch(e -> e.getValue() < 0))
			throw new IllegalAffectationException();

		consumer.accept(event);

		return affectToProduction(event);
	}

	/**
	 * Add a drafted card to the production line
	 * 
	 * @param consumer
	 * @param action
	 * @return
	 */
	public EmpirePlanningWrapper moveToProduction(ActionMoveDraftedToProduction action,
			Consumer<EmpireEvent> consumer) {
		DraftedCard dab = planning.draft(action.getTargetId());
		MoveToProductionEvent event = empire.moveToProduction(dab);

		consumer.accept(event);

		return moveToProduction(event);
	}

	/**
	 * Check if there is still something to do or not
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isEmpty() {
		return planning.isEmpty();
	}

	/**
	 * Recyle a drafted card to a production card
	 * 
	 * @param action
	 * @param consumer
	 * @return
	 */
	public EmpirePlanningWrapper recycleToProduction(ActionRecycleDraftedToProduction action,
			Consumer<EmpireEvent> consumer) {
		DraftedCard drafted = planning.draft(action.getTargetId());
		AffectProductionEvent event = empire.recycleToProduction(action.getProductionId(), drafted);

		consumer.accept(event);

		return affectToProduction(event);
	}

	/**
	 * Recycle a card to the empire resource
	 * 
	 * @param action
	 * @param consumer
	 * @return
	 */
	public EmpirePlanningWrapper recycleDrafted(ActionRecycleDrafted action, Consumer<EmpireEvent> consumer) {
		DraftedCard drafted = planning.draft(action.getTargetId());
		RecycleEvent event = empire.recycle(drafted);

		consumer.accept(event);

		return recycleDrafted(event);
	}

	public List<DraftedCard> getDrafteds() {
		return planning.getDrafteds();
	}

	public List<DraftedCard> getChoice() {
		return planning.getChoice();
	}

	private EmpirePlanningWrapper affectToProduction(AffectProductionEvent event) {
		CurrentPlanning newPlanning = planning;
		DraftedCard recycled = event.getRecycled();
		if (recycled != null)
			newPlanning = planning.draft(recycled);

		return new EmpirePlanningWrapper(empire.apply(event), newPlanning);
	}

	private EmpirePlanningWrapper moveToProduction(MoveToProductionEvent event) {
		return new EmpirePlanningWrapper(empire.apply(event), planning.draft(event.getCard()));
	}

	private EmpirePlanningWrapper recycleDrafted(RecycleEvent event) {
		return new EmpirePlanningWrapper(empire.apply(event), planning.draft(event.getRecycled()));
	}

	private EmpirePlanningWrapper recyleProduction(RecycleInProductionEvent event) {
		return new EmpirePlanningWrapper(empire.apply(event), planning);
	}
}
