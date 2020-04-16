package fr.keyser.fsm;

public interface EventEndpoint {

	public void submit(int priority, EventMsg event);

	public default void submit(EventMsg event) {
		submit(0, event);
	}
}
