package fr.keyser.wonderfull.world.game;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Function;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import fr.keyser.fsm.Instance;
import fr.keyser.wonderfull.world.PlayerGameDescription;
import fr.keyser.wonderfull.world.action.EmpirePlayAction;
import fr.keyser.wonderfull.world.dto.GameDTO;

public class InGameService {

	private final SimpMessageSendingOperations sendingOperations;

	private final GameDTOBuilder builder;

	private final ActiveGameRepository repository;

	public InGameService(ActiveGameRepository repository, SimpMessageSendingOperations sendingOperations,
			GameDTOBuilder builder) {
		this.repository = repository;
		this.sendingOperations = sendingOperations;
		this.builder = builder;
	}

	public List<PlayerGameDescription> gamesFor(String user) {
		return repository.gamesFor(user);
	}

	public void pass(String externalId) {
		process(externalId, ResolvedGame::pass);
	}

	public void play(String externalId, EmpirePlayAction action) {
		process(externalId, resolved -> resolved.play(action));
	}

	public GameDTO refresh(String externalId) {
		ResolvedGame resolved = repository.findByExternal(externalId);

		Instance<GameInfo> instance = resolved.instance();
		return builder.playerDTO(instance, resolved.getMyself());
	}

	private void process(String externalId, Function<ResolvedGame, Instance<GameInfo>> action) {
		ResolvedGame resolved = repository.findByExternal(externalId);

		Instance<GameInfo> instance = action.apply(resolved);

		repository.update(resolved);

		List<InGameId> players = resolved.getPlayers();
		List<GameDTO> dtos = builder.computeDTOs(instance);

		for (int i = 0, size = dtos.size(); i < size; ++i) {
			send(players.get(i), dtos.get(i));
		}
	}

	private void send(InGameId player, GameDTO dto) {
		String destination = MessageFormat.format("/game/{0}", player.getExternal());
		sendingOperations.convertAndSendToUser(player.getUser().getName(), destination, dto);
	}

}
