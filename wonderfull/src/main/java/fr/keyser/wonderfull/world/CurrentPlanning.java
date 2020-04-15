package fr.keyser.wonderfull.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrentPlanning {

	public static class DraftedAndPlanning {

		private final CurrentPlanning planning;

		private final DraftedCard drafted;

		private DraftedAndPlanning(CurrentPlanning build, DraftedCard drafted) {
			this.planning = build;
			this.drafted = drafted;
		}

		public CurrentPlanning getPlanning() {
			return planning;
		}

		public DraftedCard getDrafted() {
			return drafted;
		}

	}

	private final List<DraftedCard> drafteds;

	public CurrentPlanning( List<DraftedCard> drafteds) {
		this.drafteds = Collections.unmodifiableList(drafteds);
	}

	public DraftedAndPlanning draft(int targetId) {
		int index = AbstractCard.findIndex(drafteds, targetId);
		DraftedCard drafted = drafteds.get(index);
		List<DraftedCard> newDrafteds = new ArrayList<>(drafteds);
		newDrafteds.remove(index);
		CurrentPlanning newBuild = new CurrentPlanning(newDrafteds);
		return new DraftedAndPlanning(newBuild, drafted);
	}

	public List<DraftedCard> getDrafteds() {
		return drafteds;
	}

	public boolean isEmpty() {
		return drafteds.isEmpty();
	}
}
