package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.AbstractCard;

@JsonTypeName(RecycleInProductionEvent.TYPE)
public class RecycleInProductionEvent extends RecycleEvent {

	public static final String TYPE = "recycle_prod";

	private final int index;

	public RecycleInProductionEvent(RecycleEvent event, int index) {
		super(event);
		this.index = index;
	}

	@JsonCreator
	public RecycleInProductionEvent(@JsonProperty("quantity") int quantity,
			@JsonProperty("storedRawDelta") int storedRawDelta, @JsonProperty("krystaliumDelta") int krystaliumDelta,
			@JsonProperty("recycled") AbstractCard recycled, @JsonProperty("index") int index) {
		super(quantity, storedRawDelta, krystaliumDelta, recycled);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
