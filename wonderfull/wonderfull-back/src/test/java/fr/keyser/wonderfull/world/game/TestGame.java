package fr.keyser.wonderfull.world.game;

import static fr.keyser.wonderfull.world.EmpireConfiguration.empire;
import static java.util.Arrays.asList;

import java.time.Instant;
import java.util.Arrays;
import java.util.function.BiConsumer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fr.keyser.wonderfull.security.UserPrincipal;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.GameConfiguration;
import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.WonderfullModuleBuilder;
import fr.keyser.wonderfull.world.action.ActionDraft;
import fr.keyser.wonderfull.world.dto.CardPossibleActions;
import fr.keyser.wonderfull.world.dto.GameDTO;
import fr.keyser.wonderfull.world.event.EmpireEvent;
import fr.keyser.wonderfull.world.game.PlayersStatus.Status;

public class TestGame {

	private static final Logger logger = LoggerFactory.getLogger(TestGame.class);

	@Test
	void nominal() throws JsonProcessingException {

		GameConfiguration conf = new GameConfiguration(null, asList("core", "empire"), null,
				asList(empire(new UserPrincipal(null, "p0"), "krystalium"),
						empire(new UserPrincipal(null, "p1"), "krystalium"),
						empire(new UserPrincipal(null, "p2"), "basic")),
				Instant.now());

		PlayersStatus status = new PlayersStatus(
				Arrays.asList(Status.WAITING_INPUT, Status.WAITING_INPUT, Status.WAITING_INPUT));

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load(conf.getDictionaries());

		SimpleModule module = new WonderfullModuleBuilder(dictionnary).createModule();
		ObjectMapper om = new ObjectMapper().setSerializationInclusion(Include.NON_NULL).registerModule(module)
				.registerModule(new JavaTimeModule());
		ObjectWriter printer = om.writerWithDefaultPrettyPrinter();

		Game g = Game.bootstrap(loader, conf);

		g = g.nextTurn();
		GameDTO dto = g.asDTO(0, status);
		logger.debug(printer.writeValueAsString(dto));
		String str = printer.writeValueAsString(g);
		logger.debug(str);

		// reload the game with the dictionnary
		Game reloaded = om.readValue(str, Game.class).reloadDictionnary(loader);

		String strReloaded = printer.writeValueAsString(reloaded);
		Assertions.assertThat(strReloaded).isEqualTo(str);

		BiConsumer<Integer, EmpireEvent> noop = (i, e) -> {
		};

		CardPossibleActions c = dto.getActions().getCards().get(0);
		g = g.dispatch(new ActionDraft(c.getTargetId()).dispatch(0), noop);
		logger.debug(printer.writeValueAsString(g.asDTO(0, status)));
		
		
		logger.debug(printer.writeValueAsString(reloaded.scoreBoards()));

	}

	@Test
	void singlePlayer() {

		GameConfiguration conf = new GameConfiguration(null, asList("core", "empire"), Game.TO_THE_CENTER_OF_EARTH,
				asList(empire(new UserPrincipal(null, "p0"), "krystalium")), Instant.now());

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		Game g = Game.bootstrap(loader, conf);
		Empire empire = g.getEmpires().get(0).getEmpire();

		Assertions.assertThat(empire.getInProduction()).hasSize(4);
	}
}
