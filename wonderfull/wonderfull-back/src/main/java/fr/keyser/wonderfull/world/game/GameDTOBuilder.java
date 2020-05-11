package fr.keyser.wonderfull.world.game;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.keyser.fsm.Instance;
import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.dto.FullPlayerEmpireDTO;
import fr.keyser.wonderfull.world.dto.GameDTO;
import fr.keyser.wonderfull.world.game.PlayersStatus.Status;

public class GameDTOBuilder {

	private final MetaCardDictionnaryLoader loader;

	public GameDTOBuilder(MetaCardDictionnaryLoader loader) {
		this.loader = loader;
	}

	public GameDTO playerDTO(Instance<GameInfo> instance, int index) {
		PlayersStatus status = playersStatus(instance);

		Game game = instance.get(GameInfo::getGame);
		GameDTO dto = game.asDTO(index, status);
		commons(instance, dto);

		List<String> dictionaries = game.getConfiguration().getDictionaries();
		MetaCardDictionnary dictionnary = loader.load(dictionaries);
		dto.setDictionnary(dictionnary);
		dto.setWop(Extension.containsWarOrPeace(dictionaries));

		return dto;
	}

	public List<GameDTO> computeDTOs(Instance<GameInfo> instance) {
		PlayersStatus status = playersStatus(instance);

		Game game = instance.get(GameInfo::getGame);
		return IntStream.range(0, game.getPlayerCount()).mapToObj(player -> {
			GameDTO dto = game.asDTO(player, status);
			commons(instance, dto);
			return dto;
		}).collect(Collectors.toList());
	}

	private void commons(Instance<GameInfo> instance, GameDTO dto) {
		dto.setClock(instance.get(GameInfo::getClock));
		dto.setTurn(instance.get(GameInfo::getTurn));
		dto.setEvents(instance.get(GameInfo::getEvents));

		List<String> state = instance.getState().values();
		String first = state.get(0);
		if (GameAutomatsBuilder.DRAFT_STATE.equals(first)) {
			dto.setPhase("DRAFT");
		} else if (GameAutomatsBuilder.PLANNING_STATE.equals(first)) {
			dto.setPhase("PLANNING");
		} else if (GameAutomatsBuilder.PRODUCTION_STATE.equals(first)) {
			dto.setPhase("PRODUCTION");
			dto.setStep(state.get(1));
		} else if (GameAutomatsBuilder.END_STATE.equals(first)) {
			dto.setTerminated(true);
			dto.setWinner(instance.get(GameInfo::winner));

			List<PlayerScoreBoard> sbs = instance.get(GameInfo::scoreBoards);
			List<FullPlayerEmpireDTO> empires = dto.getEmpires();
			for (int i = 0; i < sbs.size(); ++i) {
				empires.get(i).setScoreBoard(sbs.get(i));
			}

		}

	}

	private PlayersStatus playersStatus(Instance<GameInfo> instance) {
		List<Instance<GameInfo>> child = instance.childsInstances();
		List<Status> sta = child.stream().map(i -> {

			String st = i.getState().toString();

			if (st.endsWith(GameAutomatsBuilder.SUPREMACY_STATE))
				return Status.WAITING_SUPREMACY;

			if (st.endsWith(GameAutomatsBuilder.WAITING_STATE))
				return Status.WAITING_INPUT;

			return Status.DONE;

		}).collect(Collectors.toList());
		return new PlayersStatus(sta);
	}

}
