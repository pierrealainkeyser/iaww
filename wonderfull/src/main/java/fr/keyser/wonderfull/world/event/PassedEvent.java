package fr.keyser.wonderfull.world.event;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(PassedEvent.TYPE)
public class PassedEvent implements EmpireEvent {

	public static final String TYPE = "pass";

	public static final PassedEvent PASS = new PassedEvent();

}
