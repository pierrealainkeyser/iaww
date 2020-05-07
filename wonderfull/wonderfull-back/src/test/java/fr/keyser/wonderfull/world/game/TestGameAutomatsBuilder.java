package fr.keyser.wonderfull.world.game;

import static fr.keyser.wonderfull.world.EmpireConfiguration.empire;
import static java.util.Arrays.asList;

import java.time.Instant;
import java.util.List;

import org.assertj.core.api.Assertions;
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
import fr.keyser.wonderfull.security.UserPrincipal;
import fr.keyser.wonderfull.world.GameConfiguration;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.action.ActionDig;
import fr.keyser.wonderfull.world.action.ActionDiscardToDig;
import fr.keyser.wonderfull.world.action.ActionDraft;
import fr.keyser.wonderfull.world.action.ActionMoveDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleDrafted;
import fr.keyser.wonderfull.world.dto.CardPossibleActions;
import fr.keyser.wonderfull.world.dto.FullPlayerEmpireDTO;
import fr.keyser.wonderfull.world.dto.GameDTO;

public class TestGameAutomatsBuilder {

	private static final Logger logger = LoggerFactory.getLogger(TestGameAutomatsBuilder.class);

	@Test
	void nominal() throws JsonProcessingException {

		GameConfiguration conf = new GameConfiguration(null, asList("core", "empire"), null,
				asList(empire(new UserPrincipal(null, "p0"), "krystalium"),
						empire(new UserPrincipal(null, "p1"), "krystalium"),
						empire(new UserPrincipal(null, "p2"), "basic")),
				Instant.now());
		GameAutomatsBuilder builder = new GameAutomatsBuilder();

		Automats<GameInfo> automats = builder.build(InstanceId.uuid(), conf.getPlayerCount(), false);
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

	@Test
	void singlePlayer() throws JsonProcessingException {

		GameConfiguration conf = new GameConfiguration(null, asList("core", "empire"), null,
				asList(empire(new UserPrincipal(null, "p0"), "krystalium")), Instant.now());
		GameAutomatsBuilder builder = new GameAutomatsBuilder();

		Automats<GameInfo> automats = builder.build(InstanceId.uuid(), conf.getPlayerCount(), false);
		logger.info("Automats {}", automats);

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();

		automats.submit(Start.start(new GameInfo(Game.bootstrap(loader, conf))));
		GameDTOBuilder dtoBuilder = new GameDTOBuilder(loader);

		ObjectMapper om = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
		ObjectWriter printer = om.writerWithDefaultPrettyPrinter();

		ActiveGame activeGame = new ActiveGame(automats);

		GameDTO dto = dtoBuilder.computeDTOs(automats.instances().get(0)).get(0);
		logger.info("DTO :{}", printer.writeValueAsString(dto));

		ResolvedGame player0 = new ResolvedGame(0, activeGame);

		FullPlayerEmpireDTO empire = dto.getEmpires().get(0);
		int id0 = empire.getDrafteds().get(0).getId();
		int id1 = empire.getDrafteds().get(1).getId();

		player0.play(new ActionDiscardToDig(new int[] { id0, id1 }));

		dto = dtoBuilder.computeDTOs(automats.instances().get(0)).get(0);
		logger.info("DTO :{}", printer.writeValueAsString(dto));

		// dig the first card
		empire = dto.getEmpires().get(0);
		
		Assertions.assertThat(empire.getDrafteds()).hasSize(3);		
		Assertions.assertThat(empire.getChoice()).hasSize(5);
		
		int id = empire.getChoice().get(0).getId();

		player0.play(new ActionDig(id));

		dto = dtoBuilder.computeDTOs(automats.instances().get(0)).get(0);
		logger.info("DTO :{}", printer.writeValueAsString(dto));
		empire = dto.getEmpires().get(0);		
		Assertions.assertThat(empire.getDrafteds()).hasSize(4);

	}

}
