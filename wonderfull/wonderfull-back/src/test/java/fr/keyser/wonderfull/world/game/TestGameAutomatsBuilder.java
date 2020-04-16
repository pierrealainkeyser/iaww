package fr.keyser.wonderfull.world.game;

import static fr.keyser.wonderfull.world.EmpireConfiguration.empire;
import static java.util.Arrays.asList;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.impl.Automats;
import fr.keyser.fsm.impl.Start;
import fr.keyser.wonderfull.world.GameConfiguration;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.action.ActionDraft;
import fr.keyser.wonderfull.world.action.ActionMoveDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleDrafted;
import fr.keyser.wonderfull.world.dto.CardPossibleActions;
import fr.keyser.wonderfull.world.dto.GameDTO;

public class TestGameAutomatsBuilder {

	private static final Logger logger = LoggerFactory.getLogger(TestGameAutomatsBuilder.class);

	@Test
	void nominal() throws JsonProcessingException {

		GameConfiguration conf = new GameConfiguration(asList("core", "empire"),
				asList(empire("p0", "krystalium"), empire("p1", "krystalium"), empire("p2", "basic")));

		GameAutomatsBuilder builder = new GameAutomatsBuilder();

		Automats<GameInfo> automats = builder.build(InstanceId.uuid(), conf.getPlayerCount());
		logger.info("Automats {}", automats);

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();

		automats.submit(Start.start(new GameInfo(Game.bootstrap(loader, conf))));
		GameDTOBuilder dtoBuilder = new GameDTOBuilder(loader);

		ObjectMapper om = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
		ObjectWriter printer = om.writerWithDefaultPrettyPrinter();

		ActiveGame activeGame = new ActiveGame(automats);

		for (int i = 0; i < 7; ++i) {
			List<GameDTO> dtos = dtoBuilder.computeDTOs(automats.instances().get(0));

			for (int j = 0; j < conf.getPlayerCount(); ++j) {
				GameDTO dto = dtos.get(j);
				CardPossibleActions c = dto.getActions().getCards().get(0);
				ResolvedGame rg = new ResolvedGame(j, activeGame);

				rg.play(new ActionDraft(c.getTargetId()));

			}
		}

		List<GameDTO> dtos = dtoBuilder.computeDTOs(automats.instances().get(0));
		logger.info("DTOS :{}", printer.writeValueAsString(dtos));

		ResolvedGame player0 = new ResolvedGame(0, activeGame);

		GameDTO dto = dtos.get(0);
		CardPossibleActions c = dto.getActions().getCards().get(0);

		player0.play(new ActionMoveDraftedToProduction(c.getTargetId()));

		// recycle all the others
		for (int i = 0; i < 6; ++i) {
			c = dtoBuilder.computeDTOs(automats.instances().get(0)).get(0).getActions().getCards().get(0);
			player0.play(new ActionRecycleDrafted(c.getTargetId()));
		}
		dtos = dtoBuilder.computeDTOs(automats.instances().get(0));
		logger.info("DTO :{}", printer.writeValueAsString(dtos.get(0)));

	}

}
