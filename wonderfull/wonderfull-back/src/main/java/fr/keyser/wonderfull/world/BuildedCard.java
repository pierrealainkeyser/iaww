package fr.keyser.wonderfull.world;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = BuildedCard.Serializer.class)
public class BuildedCard extends AbstractCard {

	public static class Serializer extends AbstractCardSerializer<BuildedCard> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2114300627030234931L;

		public Serializer() {
			super(BuildedCard.class);
		}
	}

	public BuildedCard(int id, MetaCard meta) {
		super(id, meta);
	}

	public Production getProduce() {
		return Optional.ofNullable(meta.getProduce()).orElse(Production.ZERO);
	}

	public Token getType() {
		return meta.getType();
	}

	@JsonIgnore
	public Token getScoringType() {
		return meta.getScoringType();
	}

	public Value getScoring() {
		return Optional.ofNullable(meta.getScoring()).orElse(Value.ZERO);
	}
}
