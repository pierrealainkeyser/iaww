package fr.keyser.wonderfull.world.game;

import static fr.keyser.wonderfull.world.EmpireConfiguration.empire;
import static java.util.Arrays.asList;

import java.time.Instant;

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

import fr.keyser.fsm.InstanceId;
import fr.keyser.fsm.impl.Automats;
import fr.keyser.fsm.impl.AutomatsMemento;
import fr.keyser.wonderfull.security.UserPrincipal;
import fr.keyser.wonderfull.world.GameConfiguration;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.UnresolvedGameIdException;
import fr.keyser.wonderfull.world.WonderfullModuleBuilder;

public class TestGameBootstraper {

	private static final Logger logger = LoggerFactory.getLogger(TestGameBootstraper.class);

	@Test
	void bootstrap() {

		GameConfiguration conf = new GameConfiguration(null, asList("core", "empire"), null,
				asList(empire(new UserPrincipal("p0", "p0"), "krystalium"),
						empire(new UserPrincipal("p1", "p1"), "krystalium"),
						empire(new UserPrincipal("p2", "p2"), "basic")),
				Instant.now());

		GameAutomatsBuilder builder = new GameAutomatsBuilder();

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		ActiveGameMapRepository repository = new ActiveGameMapRepository();

		GameBootstraper bootstraper = new GameBootstraper(builder, loader, repository);
		ActiveGame started = bootstraper.starts(conf);

		Assertions.assertThat(started).isNotNull();
		String external = started.configuration().getEmpires().get(0).getExternalId();
		ResolvedGame byExternal = repository.findByExternal(external);
		Assertions.assertThat(byExternal).isNotNull();

		bootstraper.stop(started.getId());

		try {
			repository.findByExternal(external);
			Assertions.failBecauseExceptionWasNotThrown(UnresolvedGameIdException.class);
		} catch (UnresolvedGameIdException uge) {
			// noop
		}
	}

	@Test
	void backup() throws JsonProcessingException {
		GameConfiguration conf = new GameConfiguration(null, asList("core", "empire"), null,
				asList(empire(new UserPrincipal("p0", "p0"), "krystalium"),
						empire(new UserPrincipal("p1", "p1"), "krystalium"),
						empire(new UserPrincipal("p2", "p2"), "basic")),
				Instant.now());

		GameAutomatsBuilder builder = new GameAutomatsBuilder();

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		ActiveGameMapRepository repository = new ActiveGameMapRepository();

		GameBootstraper bootstraper = new GameBootstraper(builder, loader, repository);
		GameLoader gameLoader = new GameLoader(builder, loader);
		ActiveGame started = bootstraper.starts(conf);

		InstanceId id = started.getId();

		Automats<GameInfo> automats = started.getAutomats();

		SimpleModule module = new WonderfullModuleBuilder(loader.load(conf.getDictionaries())).createModule();
		ObjectMapper om = new ObjectMapper().setSerializationInclusion(Include.NON_NULL).registerModule(module)
				.registerModule(new JavaTimeModule());
		ObjectWriter printer = om.writerWithDefaultPrettyPrinter();
		AutomatsMemento mementos = automats.memento();
		String stored = printer.writeValueAsString(mementos);
		logger.debug(stored);

		AutomatsMemento read = om.readValue(stored, AutomatsMemento.class).map(payload -> {
			if (payload != null) {
				return om.convertValue(payload, GameInfo.class);
			}
			return null;
		});

		bootstraper.stop(id);
		Assertions.assertThat(repository.findById(id)).isNull();

		ActiveGame reloaded = gameLoader.reload(read);
		Assertions.assertThat(repository.findById(id)).isNull();
		Assertions.assertThat(reloaded).isNotNull();
	}

}
