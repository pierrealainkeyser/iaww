package fr.keyser.wonderfull.world;

import java.util.List;

import fr.keyser.fsm.InstanceId;

public class ActiveGameDescription extends AbstractGameDescription {

	private final InstanceId id;

	public ActiveGameDescription(InstanceId id, List<String> dictionaries, List<String> users) {
		super(dictionaries, users);
		this.id = id;
	}

	public InstanceId getId() {
		return id;
	}

}
