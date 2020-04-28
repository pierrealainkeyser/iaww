package fr.keyser.wonderfull.world;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {

	private final int constant;

	private final Tokens empire;

	public final static Value ZERO = new Value(0, null);

	public static Value constant(int constant) {
		return new Value(constant, null);
	}

	public static Value empire(Tokens empire) {
		return new Value(0, empire);
	}

	@JsonCreator
	public Value(@JsonProperty("constant") int constant, @JsonProperty("empire") Tokens empire) {
		this.constant = constant;
		this.empire = Optional.ofNullable(empire).orElse(Tokens.ZERO);
	}

	public int resolve(Tokens inEmpire) {
		if (constant < 0) {
			return empire.keySet().stream().mapToInt(inEmpire::get).min().orElse(0) * -constant;
		} else {
			return constant + empire.entrySet().stream().mapToInt(e -> inEmpire.get(e.getKey()) * e.getValue()).sum();
		}
	}

	@Override
	public String toString() {
		if (constant > 0)
			return constant + "";
		else if (constant < 0)
			return (-constant) + "*" + empire;
		else
			return empire.toString();
	}

	public int getConstant() {
		return constant;
	}

	public Tokens getEmpire() {
		return empire;
	}

	@Override
	public int hashCode() {
		return Objects.hash(constant, empire);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value other = (Value) obj;
		return constant == other.constant && Objects.equals(empire, other.empire);
	}

}
