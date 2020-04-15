package fr.keyser.wonderfull.world;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class Production {

	public static final Production ZERO = new Production(Collections.emptyMap());

	private final Map<Token, Value> values;

	@JsonCreator
	public Production(@JsonUnwrapped Map<Token, Value> values) {
		this.values = values;
	}

	@JsonUnwrapped
	public Map<Token, Value> getValues() {
		return values;
	}

	public int resolve(Token type, Tokens empire) {
		Value value = Optional.ofNullable(values.get(type)).orElse(Value.ZERO);
		return value.resolve(empire);
	}

	@Override
	public int hashCode() {
		return Objects.hash(values);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Production other = (Production) obj;
		return Objects.equals(values, other.values);
	}

	@Override
	public String toString() {
		return values.toString();
	}

}
