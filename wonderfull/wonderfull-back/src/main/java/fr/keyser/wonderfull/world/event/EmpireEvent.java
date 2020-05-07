package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = AffectProductionEvent.class, name = AffectProductionEvent.TYPE),
		@JsonSubTypes.Type(value = DraftEvent.class, name = DraftEvent.TYPE),
		@JsonSubTypes.Type(value = MoveToProductionEvent.class, name = MoveToProductionEvent.TYPE),
		@JsonSubTypes.Type(value = AffectProductionEvent.class, name = AffectProductionEvent.TYPE),
		@JsonSubTypes.Type(value = PassedEvent.class, name = PassedEvent.TYPE),
		@JsonSubTypes.Type(value = RecycleEvent.class, name = RecycleEvent.TYPE),
		@JsonSubTypes.Type(value = RecycleInProductionEvent.class, name = RecycleInProductionEvent.TYPE),
		@JsonSubTypes.Type(value = SupremacyEvent.class, name = SupremacyEvent.TYPE),
		@JsonSubTypes.Type(value = UndoEvent.class, name = UndoEvent.TYPE) ,
		@JsonSubTypes.Type(value = DigEvent.class, name = DigEvent.TYPE),
		@JsonSubTypes.Type(value = DiscardToDigEvent.class, name = DiscardToDigEvent.TYPE) }
		)
public interface EmpireEvent {
}
