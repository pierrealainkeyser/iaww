package fr.keyser.wonderfull.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = InProductionCard.Serializer.class)
public class InProductionCard extends AbstractCard {

	public static class Serializer extends AbstractCardSerializer<InProductionCard> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2114300627030234931L;

		public Serializer() {
			super(InProductionCard.class);
		}

		protected void more(InProductionCard value, JsonGenerator gen, SerializerProvider provider) throws IOException {

			gen.writeFieldName("slots");
			gen.writeStartArray();
			for (ProductionSlot slot : value.slots)
				gen.writeObject(slot);
			gen.writeEndArray();
		}

	}

	protected final List<ProductionSlot> slots;

	public InProductionCard(int id, MetaCard meta, List<ProductionSlot> slots) {
		super(id, meta);
		this.slots = Collections.unmodifiableList(slots);
	}

	public InProductionCard add(Map<Integer, Token> slots) {

		List<ProductionSlot> newSlots = new ArrayList<>(this.slots);
		for (Entry<Integer, Token> e : slots.entrySet()) {
			int index = e.getKey();
			Token type = e.getValue();
			ProductionSlot current = newSlots.get(index);

			newSlots.set(index,
					current.updateStatus(Token.KRYSTALIUM == type ? ProductionStatus.KRYSTALIUM : ProductionStatus.OK));
		}

		return new InProductionCard(id, meta, newSlots);
	}

	private void loopEmptySlot(BiConsumer<Integer, ProductionSlot> loop) {
		for (int i = 0; i < slots.size(); ++i) {
			ProductionSlot slot = slots.get(i);
			if (slot.empty())
				loop.accept(i, slot);
		}
	}

	public int firstEmptySlot(Token type) {
		for (int i = 0; i < slots.size(); ++i) {
			ProductionSlot slot = slots.get(i);
			if (slot.empty() && slot.getType() == type)
				return i;
		}
		return -1;
	}

	public Map<Integer, List<Token>> affectablesSlots(Tokens available) {
		Map<Integer, List<Token>> out = new TreeMap<>();
		loopEmptySlot((i, slot) -> {
			Token type = slot.getType();

			boolean has = available.has(type);
			boolean repleacable = type.repleacable();
			if (!has && repleacable) {
				type = Token.KRYSTALIUM;
				has = available.has(type);
			}

			if (has) {
				out.put(i,
						repleacable ? Arrays.asList(slot.getType(), Token.KRYSTALIUM) : Arrays.asList(slot.getType()));
			}
		});

		return out;
	}

	public boolean require(Token type) {
		return slots.stream().anyMatch(s -> type == s.getType() && s.empty());
	}

	public Optional<BuildedCard> builded() {
		boolean builded = slots.stream().allMatch(ProductionSlot::ok);
		if (builded)
			return Optional.of(new BuildedCard(id, meta));
		else
			return Optional.empty();
	}

	public Tokens getBonus() {
		return Optional.ofNullable(meta.getBonus()).orElse(Tokens.ZERO);
	}
}
