package fr.keyser.wonderfull.world;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AbstractCardSerializer<T extends AbstractCard> extends StdSerializer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3351512427441575284L;

	protected AbstractCardSerializer(Class<T> type) {
		super(type);
	}

	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeFieldName("id");
		gen.writeNumber(value.getId());
		gen.writeFieldName("name");
		gen.writeString(value.getName());
		more(value, gen, provider);
		gen.writeEndObject();
	}

	protected void more(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {

	}
}