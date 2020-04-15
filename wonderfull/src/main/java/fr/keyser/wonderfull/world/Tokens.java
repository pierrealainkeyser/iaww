package fr.keyser.wonderfull.world;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Tokens {

	private static final String DELIMITER = ", ";

	@FunctionalInterface
	public static interface TokenReducer {
		public int reduce(int left, int right);
	}

	private static Map<Token, Integer> reduce(Tokens left, Tokens right, TokenReducer reducer) {
		Map<Token, Integer> cur = new EnumMap<>(Token.class);
		for (Entry<Token, Integer> e : left.entrySet()) {
			Token key = e.getKey();
			int reduced = reducer.reduce(e.getValue(), right.get(key));

			cur.put(key, reduced);
		}

		for (Entry<Token, Integer> e : right.entrySet()) {
			Token key = e.getKey();
			if (!cur.containsKey(key)) {
				int reduced = reducer.reduce(0, e.getValue());
				cur.put(key, reduced);
			}
		}

		Iterator<Integer> it = cur.values().iterator();
		while (it.hasNext()) {
			if (0 == it.next()) {
				it.remove();
			}
		}

		return cur;

	}

	private final Map<Token, Integer> values;

	public final static Tokens ZERO = new Tokens(Collections.emptyMap());

	@JsonCreator
	public Tokens(String value) {
		this(parse(value));
	}

	private Tokens(Map<Token, Integer> values) {
		this.values = Collections.unmodifiableMap(values);
	}

	Tokens(Token token, int value) {
		this(Map.of(token, value));
	}

	public Map<Token, Integer> asMap() {
		return values;
	}

	public Tokens add(Tokens token) {
		return new Tokens(reduce(this, token, Math::addExact));
	}

	public Tokens min(Tokens token) {
		return new Tokens(reduce(this, token, Math::min));
	}

	public Tokens subtract(Tokens token) {
		return new Tokens(reduce(this, token, Math::subtractExact));
	}

	public Tokens floor() {
		return new Tokens(entrySet().stream().filter(e -> e.getValue() > 0)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
	}

	@Override
	@JsonValue
	public String toString() {
		return entrySet().stream().map(e -> {
			int count = e.getValue();
			String name = e.getKey().name();
			if (count == 1)
				return name;
			else
				return name + "*" + count;

		}).collect(Collectors.joining(DELIMITER));
	}

	private static Map<Token, Integer> parse(String s) {
		if ("".equals(s))
			return Collections.emptyMap();

		Map<Token, Integer> out = new LinkedHashMap<>();
		for (String raw : s.split(DELIMITER)) {
			String[] n = raw.split("\\*");
			Token token = Token.valueOf(n[0]);
			int count = 1;
			if (n.length == 2)
				count = Integer.parseInt(n[1]);
			out.put(token, count);
		}

		return out;
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public Set<Entry<Token, Integer>> entrySet() {
		return values.entrySet();
	}

	public boolean has(Token token) {
		return get(token) > 0;
	}

	public int get(Token token) {
		return Optional.ofNullable(values.get(token)).orElse(0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tokens other = (Tokens) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	public Set<Token> keySet() {
		return values.keySet();
	}
}