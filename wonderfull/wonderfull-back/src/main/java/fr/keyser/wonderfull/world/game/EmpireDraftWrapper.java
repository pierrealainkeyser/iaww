package fr.keyser.wonderfull.world.game;

import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.wonderfull.world.CurrentDraft;
import fr.keyser.wonderfull.world.DraftableCard;
import fr.keyser.wonderfull.world.DraftedCard;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.action.ActionDraft;
import fr.keyser.wonderfull.world.event.DraftEvent;
import fr.keyser.wonderfull.world.event.EmpireEvent;

public class EmpireDraftWrapper extends EmpireWrapper {

	private final CurrentDraft draft;

	public EmpireDraftWrapper(Empire empire, CurrentDraft draft) {
		super(empire);
		this.draft = draft;
	}

	public EmpireDraftWrapper(@JsonProperty("empire") Empire empire,
			@JsonProperty("drafteds") List<DraftedCard> drafteds, @JsonProperty("inHand") List<DraftableCard> inHand) {
		this(empire, new CurrentDraft(drafteds, inHand));

	}

	/**
	 * Draft a card from hand
	 * 
	 * @param action
	 * @return
	 */
	public EmpireDraftWrapper draft(ActionDraft action, Consumer<EmpireEvent> consumer) {
		DraftEvent event = draft.draft(action.getTargetId());
		consumer.accept(event);
		return draft(event);
	}

	public EmpireDraftWrapper draft(DraftEvent event) {
		return new EmpireDraftWrapper(empire, draft.draft(event));
	}

	/**
	 * Check if there is still something to do or not
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isEmpty() {
		return draft.isEmpty();
	}

	public List<DraftedCard> getDrafteds() {
		return draft.getDrafteds();
	}

	public List<DraftableCard> getInHand() {
		return draft.getInHand();
	}

}
