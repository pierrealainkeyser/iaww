package fr.keyser.wonderfull.world;

import java.io.IOException;
import java.util.function.BiFunction;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ProtoCardDeserializer<T extends AbstractCard> extends StdDeserializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3243721480198131774L;

	private final MetaCardDictionnary dictionnary;

	private final BiFunction<Integer, MetaCard, T> constructor;

	public ProtoCardDeserializer(Class<T> type, BiFunction<Integer, MetaCard, T> constructor,
			MetaCardDictionnary dictionnary) {
		super(type);
		this.constructor = constructor;
		this.dictionnary = dictionnary;
	}

	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ProtoCard proto = p.readValueAs(ProtoCard.class);
		MetaCard meta = dictionnary.resolve(proto.getName());
		return constructor.apply(proto.getId(), meta);
	}

}
