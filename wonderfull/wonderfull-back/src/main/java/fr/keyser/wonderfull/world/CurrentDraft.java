package fr.keyser.wonderfull.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.keyser.wonderfull.world.event.DraftEvent;

public class CurrentDraft {

	private final List<DraftedCard> drafteds;

	private final List<DraftableCard> inHand;

	public CurrentDraft(List<DraftedCard> drafteds, List<DraftableCard> inHand) {
		this.drafteds = Collections.unmodifiableList(drafteds);
		this.inHand = Collections.unmodifiableList(inHand);
	}

	public CurrentDraft withHand(List<DraftableCard> inHand) {
		return new CurrentDraft(drafteds, inHand);
	}

	public DraftEvent draft(int targetId) {
		int index = AbstractCard.findIndex(inHand, targetId);
		DraftableCard card = inHand.get(index);
		return new DraftEvent(card, index);
	}

	public CurrentDraft draft(DraftEvent event) {

		int index = event.getIndex();
		List<DraftableCard> newHands = new ArrayList<>(inHand);
		DraftedCard drafted = newHands.remove(index).draft();

		List<DraftedCard> newDrafteds = new ArrayList<>(drafteds.size() + 1);
		newDrafteds.addAll(drafteds);
		newDrafteds.add(drafted);

		return new CurrentDraft(newDrafteds, newHands);
	}

	@JsonIgnore
	public boolean isEmpty() {
		return inHand.isEmpty();
	}

	public List<DraftedCard> getDrafteds() {
		return drafteds;
	}

	public List<DraftableCard> getInHand() {
		return inHand;
	}
}
