package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(UndoEvent.TYPE)
public class UndoEvent implements EmpireEvent {

	public static final String TYPE = "undo";

	public static final UndoEvent UNDO = new UndoEvent();

}
