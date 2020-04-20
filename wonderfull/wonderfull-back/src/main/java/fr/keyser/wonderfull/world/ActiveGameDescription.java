package fr.keyser.wonderfull.world;

import java.time.Instant;
import java.util.List;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.security.UserPrincipal;

public class ActiveGameDescription extends AbstractGameDescription {

	private final InstanceId id;

	public ActiveGameDescription(InstanceId id, String creator, List<String> dictionaries, String startingEmpire,
			List<UserPrincipal> users, Instant createdAt) {
		super(creator, dictionaries, startingEmpire, users, createdAt);
		this.id = id;
	}

	public InstanceId getId() {
		return id;
	}

}
