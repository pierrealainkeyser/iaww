package fr.keyser.wonderfull.world;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = DraftedCard.Serializer.class)
public class DraftedCard extends AbstractCard {

	public static class Serializer extends AbstractCardSerializer<DraftedCard> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2114300627030234931L;

		public Serializer() {
			super(DraftedCard.class);
		}
	}

	public DraftedCard(int id, MetaCard meta) {
		super(id, meta);
	}

	public Token getRecycle() {
		return meta.getRecycle();
	}

	public InProductionCard produce() {

		List<ProductionSlot> slots = meta.getCost().entrySet().stream().flatMap(e -> {
			Token token = e.getKey();
			int count = e.getValue();
			return IntStream.range(0, count).mapToObj(i -> new ProductionSlot(token));
		}).collect(Collectors.toList());

		return new InProductionCard(id, meta, slots);
	}

}
