package fr.keyser.wonderfull.world;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class InProductionCardDeserializer extends StdDeserializer<InProductionCard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3243721480198131774L;

	private final MetaCardDictionnary dictionnary;

	public static class InProductionProtoCard {

		@JsonProperty
		private int id;

		@JsonProperty
		private String name;

		@JsonProperty
		private List<ProductionSlot> slots;

	}

	public InProductionCardDeserializer(MetaCardDictionnary dictionnary) {
		super(InProductionCard.class);
		this.dictionnary = dictionnary;
	}

	@Override
	public InProductionCard deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		InProductionProtoCard proto = p.readValueAs(InProductionProtoCard.class);
		MetaCard meta = dictionnary.resolve(proto.name);

		return new InProductionCard(proto.id, meta, proto.slots);
	}

}
