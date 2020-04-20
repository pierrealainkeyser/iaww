package fr.keyser.wonderfull.world;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.security.UserPrincipal;

public class GameConfiguration {

	private final List<String> dictionaries;

	private final List<EmpireConfiguration> empires;

	private final Instant createdAt;

	private final String creator;

	private final String startingEmpire;

	@JsonCreator
	public GameConfiguration(@JsonProperty("creator") String creator,
			@JsonProperty("dictionaries") List<String> dictionaries,
			@JsonProperty("startingEmpire") String startingEmpire,
			@JsonProperty("empires") List<EmpireConfiguration> empires, @JsonProperty("createdAt") Instant createdAt) {
		this.creator = creator;
		this.startingEmpire = startingEmpire;
		this.dictionaries = dictionaries;
		this.empires = empires;
		this.createdAt = createdAt;
	}

	public String getCreator() {
		return creator;
	}

	public String getStartingEmpire() {
		return startingEmpire;
	}

	public PlayerGameDescription asDescription(InstanceId id, String user, boolean terminated) {
		String externalId = empires.stream().filter(e -> e.getUser().getName().equals(user))
				.map(EmpireConfiguration::getExternalId).findFirst().orElse(null);
		return new PlayerGameDescription(id, externalId, terminated, creator, dictionaries, startingEmpire, usersList(),
				createdAt);

	}

	public ActiveGameDescription asGameDescription(InstanceId id) {
		return new ActiveGameDescription(id, creator, dictionaries, startingEmpire, usersList(), createdAt);
	}

	public List<String> getDictionaries() {
		return dictionaries;
	}

	public List<EmpireConfiguration> getEmpires() {
		return empires;
	}

	@JsonIgnore
	public int getPlayerCount() {
		return empires.size();
	}

	@Override
	public String toString() {
		return String.format("[empires=%s, dictionaries=%s]", empires, dictionaries);
	}

	private List<UserPrincipal> usersList() {
		return empires.stream().map(EmpireConfiguration::getUser).collect(Collectors.toList());
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
