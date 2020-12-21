package fr.keyser.wonderfull.world;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {

	private final int constant;

	private final Tokens empire;

	private final Tokens dual;

	public final static Value ZERO = new Value(0, null, null);

	public static Value constant(int constant) {
		return new Value(constant, null, null);
	}

	public static Value empire(Tokens empire) {
		return new Value(0, empire, null);
	}

	@JsonCreator
	public Value(@JsonProperty("constant") int constant, @JsonProperty("empire") Tokens empire,
			@JsonProperty("dual") Tokens dual) {
		this.constant = constant;
		this.empire = Optional.ofNullable(empire).orElse(Tokens.ZERO);
		this.dual = Optional.ofNullable(dual).orElse(Tokens.ZERO);
	}

	public int resolve(Tokens inEmpire) {
		return constant
				+ resolveEmpire(inEmpire)
				+ resolveDual(inEmpire);
	}

	private int resolveEmpire(Tokens inEmpire) {
		return empire.entrySet().stream().mapToInt(e -> inEmpire.get(e.getKey()) * e.getValue()).sum();
	}

	private int resolveDual(Tokens inEmpire) {
		Map<Token, Integer> map = dual.asMap();
		if (map.size() == 2) {
			int mult = map.values().iterator().next();
			int min = map.keySet().stream().mapToInt(t -> inEmpire.get(t)).min().orElse(0);
			return min * mult;
		}
		return 0;
	}

	@Override
	public String toString() {
		if (constant > 0)
			return constant + "";
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
		return Objects.hash(constant, dual, empire);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Value))
			return false;
		Value other = (Value) obj;
		return constant == other.constant && Objects.equals(dual, other.dual) && Objects.equals(empire, other.empire);
	}

	public Tokens getDual() {
		return dual;
	}

}
