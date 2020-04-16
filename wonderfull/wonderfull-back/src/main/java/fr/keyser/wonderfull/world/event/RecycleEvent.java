package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.AbstractCard;

@JsonTypeName(RecycleEvent.TYPE)
public class RecycleEvent implements EmpireEvent {

	public static final String TYPE = "recycle";

	private final int quantity;

	private final int storedRawDelta;

	private final int krystaliumDelta;

	private final AbstractCard recycled;

	public RecycleEvent(RecycleEvent event) {
		this(event.getQuantity(), event.getStoredRawDelta(), event.getKrystaliumDelta(), event.getRecycled());
	}

	@JsonCreator
	public RecycleEvent(@JsonProperty("quantity") int quantity, @JsonProperty("storedRawDelta") int storedRawDelta,
			@JsonProperty("krystaliumDelta") int krystaliumDelta, @JsonProperty("recycled") AbstractCard recycled) {
		this.quantity = quantity;
		this.storedRawDelta = storedRawDelta;
		this.krystaliumDelta = krystaliumDelta;
		this.recycled = recycled;
	}

	public int getStoredRawDelta() {
		return storedRawDelta;
	}

	public int getKrystaliumDelta() {
		return krystaliumDelta;
	}

	public AbstractCard getRecycled() {
		return recycled;
	}

	public int getQuantity() {
		return quantity;
	}

}
