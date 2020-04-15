package fr.keyser.fsm.impl;

import java.util.concurrent.ScheduledExecutorService;

import fr.keyser.fsm.TimerParameter;

public class TimerRepository {

	private ScheduledExecutorService scheduler;

	public boolean match(RegisteredTimer timer) {
		return true;
	}

	public void register(InstanceState is, TimerParameter parameter) {

	}

	public void unregister(InstanceState is) {

	}

}
