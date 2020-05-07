package fr.keyser.wonderfull.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fr.keyser.wonderfull.world.action.ActionDiscardToDig;
import fr.keyser.wonderfull.world.event.DigEvent;
import fr.keyser.wonderfull.world.event.DiscardToDigEvent;
import fr.keyser.wonderfull.world.game.IllegalActionException;

public class CurrentPlanning {

	private final List<DraftedCard> drafteds;

	private final List<DraftedCard> choice;

	public CurrentPlanning(List<DraftedCard> drafteds, List<DraftedCard> choice) {
		this.drafteds = Collections.unmodifiableList(drafteds);
		this.choice = Collections.unmodifiableList(choice);
	}

	public DigEvent dig(int targetId) {
		int index = AbstractCard.findIndex(choice, targetId);
		DraftedCard card = choice.get(index);
		return new DigEvent(card, index);
	}

	public CurrentPlanning dig(DigEvent event) {
		int index = event.getIndex();
		DraftedCard drafted = choice.get(index);

		List<DraftedCard> newDrafteds = new ArrayList<>(drafteds.size() + 1);
		newDrafteds.addAll(drafteds);
		newDrafteds.add(drafted);

		return new CurrentPlanning(newDrafteds, Collections.emptyList());
	}

	public CurrentPlanning discard(DiscardToDigEvent event, List<DraftedCard> choice) {

		// remove from drafted
		List<Integer> indexes = event.getIndexes();

		int size = drafteds.size();
		List<DraftedCard> newDrafteds = new ArrayList<>(size - indexes.size());
		for (int i = 0; i < size; ++i) {
			if (!indexes.contains(i)) {
				newDrafteds.add(drafteds.get(i));
			}
		}

		return new CurrentPlanning(newDrafteds, choice);
	}

	public DiscardToDigEvent discardToDig(ActionDiscardToDig action, List<DraftedCard> choice) {
		int[] targetsIds = action.getTargetsIds();

		int nb = targetsIds.length;
		if (2 != nb)
			throw new IllegalActionException("discardToDig");

		List<DraftedCard> cards = new ArrayList<>(nb);
		List<Integer> indexes = new ArrayList<>(nb);

		for (int i = 0; i < nb; ++i) {
			int index = AbstractCard.findIndex(drafteds, targetsIds[i]);
			cards.add(drafteds.get(index));
			indexes.add(index);
		}

		return new DiscardToDigEvent(cards, indexes, choice);
	}

	public DraftedCard draft(int targetId) {
		int index = AbstractCard.findIndex(drafteds, targetId);
		return drafteds.get(index);
	}

	public CurrentPlanning draft(AbstractCard card) {
		List<DraftedCard> newDrafteds = new ArrayList<>(drafteds);
		Iterator<DraftedCard> it = newDrafteds.iterator();
		while (it.hasNext()) {
			DraftedCard next = it.next();
			if (next.getId() == card.getId()) {
				it.remove();
				break;
			}
		}

		return new CurrentPlanning(newDrafteds, choice);
	}

	public List<DraftedCard> getChoice() {
		return choice;
	}

	public List<DraftedCard> getDrafteds() {
		return drafteds;
	}

	public boolean isEmpty() {
		return drafteds.isEmpty();
	}
}
