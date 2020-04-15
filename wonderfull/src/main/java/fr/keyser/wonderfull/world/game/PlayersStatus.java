package fr.keyser.wonderfull.world.game;

import java.util.Collections;
import java.util.List;

public class PlayersStatus {

	public enum Status {
		WAITING_INPUT, WAITING_SUPREMACY, DONE;
	}

	private final List<Status> status;

	public PlayersStatus(List<Status> status) {
		this.status = Collections.unmodifiableList(status);
	}

	public boolean isDone(int index) {
		if (status.size() <= index)
			return true;

		return Status.DONE == status.get(index);
	}

	public boolean isReady(int index) {
		if (status.size() <= index)
			return false;

		return Status.WAITING_INPUT == status.get(index);
	}

	public boolean isWaitingSupremacy(int index) {
		if (status.size() <= index)
			return false;

		return Status.WAITING_SUPREMACY == status.get(index);
	}

}
