package fr.keyser.wonderfull.world;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = DraftableCard.Serializer.class)
public class DraftableCard extends AbstractCard {

	public static class Serializer extends AbstractCardSerializer<DraftableCard> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2114300627030234931L;

		public Serializer() {
			super(DraftableCard.class);
		}
	}

	public DraftableCard(int id, MetaCard meta) {
		super(id, meta);
	}

	public DraftedCard draft() {
		return new DraftedCard(id, meta);
	}

}
