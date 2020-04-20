package fr.keyser.wonderfull.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CurrentPlanning {

	private final List<DraftedCard> drafteds;

	public CurrentPlanning(List<DraftedCard> drafteds) {
		this.drafteds = Collections.unmodifiableList(drafteds);
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

		return new CurrentPlanning(newDrafteds);
	}

	public List<DraftedCard> getDrafteds() {
		return drafteds;
	}

	public boolean isEmpty() {
		return drafteds.isEmpty();
	}
}
