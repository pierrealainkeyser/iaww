package fr.keyser.wonderfull.world.game;

import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import fr.keyser.wonderfull.world.CurrentProduction;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.Tokens;
import fr.keyser.wonderfull.world.action.ActionAffectToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleProduction;
import fr.keyser.wonderfull.world.action.ActionSupremacy;
import fr.keyser.wonderfull.world.event.AffectProductionEvent;
import fr.keyser.wonderfull.world.event.EmpireEvent;
import fr.keyser.wonderfull.world.event.RecycleEvent;
import fr.keyser.wonderfull.world.event.RecycleInProductionEvent;
import fr.keyser.wonderfull.world.event.SupremacyEvent;

public class EmpireProductionWrapper extends EmpireWrapper {

	private final CurrentProduction production;

	/**
	 * Create a wrapper for a step
	 * 
	 * @param empire
	 * @param step
	 */
	public EmpireProductionWrapper(Empire empire, Token step) {
		this(empire, new CurrentProduction(step, empire.producedAt(step)));
	}

	public EmpireProductionWrapper(Empire empire, @JsonUnwrapped CurrentProduction production) {
		super(empire);
		this.production = production.sync(empire.getOnEmpire());
	}

	@JsonCreator
	public EmpireProductionWrapper(@JsonProperty("empire") Empire empire, @JsonProperty("step") Token step,
			@JsonProperty("available") Tokens available) {
		super(empire);
		this.production = new CurrentProduction(step, available);
	}

	/**
	 * Get the produced value for the step
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getRemaining() {
		return production.getRemaining();
	}

	/**
	 * The affectation is done
	 * 
	 * @param consumer
	 * 
	 * @return
	 */
	public Empire done(Consumer<EmpireEvent> consumer) {
		int remaining = getRemaining();
		if (remaining > 0) {
			RecycleEvent event = empire.recycle(remaining);
			consumer.accept(event);
			return empire.apply(event);
		} else
			return empire;
	}

	/**
	 * Add the supremacy token
	 * 
	 * @param action
	 * @param consumer
	 * @return
	 */
	public EmpireProductionWrapper supremacy(ActionSupremacy action, Consumer<EmpireEvent> consumer) {
		Tokens tokens = production.computeSupremacy(action.isGeneral());

		consumer.accept(new SupremacyEvent(tokens));

		return new EmpireProductionWrapper(empire.addTokens(tokens), production);
	}

	/**
	 * Recyle a card in the production line
	 * 
	 * @param action
	 * @param consumer
	 * @return
	 */
	public EmpireProductionWrapper recycleProduction(ActionRecycleProduction action, Consumer<EmpireEvent> consumer) {

		RecycleInProductionEvent event = empire.recycleProduction(action.getTargetId());

		consumer.accept(event);

		return new EmpireProductionWrapper(empire.apply(event), production);
	}

	/**
	 * Affect some currently produced resources to a card in production
	 * 
	 * @param consumer
	 * @param action
	 * @return
	 */
	public EmpireProductionWrapper affectToProduction(ActionAffectToProduction action, Consumer<EmpireEvent> consumer) {

		Tokens consumed = action.getConsumed();
		Tokens available = production.getAvailable();
		// check affectation
		Tokens remaining = available.subtract(consumed);
		if (remaining.entrySet().stream().anyMatch(e -> e.getValue() < 0))
			throw new IllegalAffectationException();

		CurrentProduction newProduction = production.affect(consumed);

		AffectProductionEvent event = empire.affectToProduction(action.getTargetId(), action.getSlots());

		consumer.accept(event);

		Empire newEmpire = empire.apply(event);
		return new EmpireProductionWrapper(newEmpire, newProduction);
	}

	public Tokens getAvailable() {
		return production.getAvailable();
	}

	public Token getStep() {
		return production.getStep();
	}

}
