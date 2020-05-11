package fr.keyser.wonderfull.world.game;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

import fr.keyser.wonderfull.world.BuildedCard;
import fr.keyser.wonderfull.world.DraftableCard;
import fr.keyser.wonderfull.world.DraftedCard;
import fr.keyser.wonderfull.world.Empire;
import fr.keyser.wonderfull.world.MetaCard;
import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.WonderfullModuleBuilder;
import fr.keyser.wonderfull.world.action.ActionAffectToProduction;
import fr.keyser.wonderfull.world.action.ActionDraft;
import fr.keyser.wonderfull.world.action.ActionMoveDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionRecycleDraftedToProduction;
import fr.keyser.wonderfull.world.action.ActionSupremacy;
import fr.keyser.wonderfull.world.event.EmpireEvent;

public class TestPlayerEmpire {

	private static final Logger logger = LoggerFactory.getLogger(TestPlayerEmpire.class);

	@Test
	void wop() {

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load(Arrays.asList("empire", "wop"));

		MetaCard cargoFleet = dictionnary.resolve("cargo_fleet");
		MetaCard gaussianWeapons = dictionnary.resolve("gaussian_weapons");

		BuildedCard cf = new DraftableCard(1, cargoFleet).draft().produce()
				.add(Map.of(0, Token.KRYSTALIUM, 1, Token.KRYSTALIUM, 2, Token.KRYSTALIUM, 3, Token.KRYSTALIUM))
				.builded().get();

		BuildedCard gw = new DraftableCard(2, gaussianWeapons).draft().produce()
				.add(Map.of(0, Token.KRYSTALIUM, 1, Token.KRYSTALIUM, 2, Token.KRYSTALIUM, 3, Token.KRYSTALIUM))
				.builded().get();

		PlayerEmpire pe = new PlayerEmpire(
				new Empire(Arrays.asList(cf, gw), Collections.emptyList(), Token.BUSINESSMAN.token(1), 0))
						.startProductionStep(Token.KRYSTALIUM);
		Empire empire = pe.resolveEmpire();

		assertThat(empire.getOnEmpire()).satisfies(t -> {
			assertThat(t.get(Token.KRYSTALIUM)).isEqualTo(1);
			assertThat(t.get(Token.BUSINESSMAN)).isEqualTo(2);
		});

		assertThat(pe.getProduction().getAvailable()).satisfies(t -> {
			assertThat(t.get(Token.KRYSTALIUM)).isEqualTo(1);
			assertThat(t.get(Token.BUSINESSMAN)).isEqualTo(2);
		});

		assertThat(empire.scoreBoard().getContributions()).containsEntry(Token.BUSINESSMAN, 2);

	}

	@Test
	void nominal() throws JsonProcessingException {

		Consumer<EmpireEvent> consumer = evt -> logger.debug("Event : {}", evt);

		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load(Arrays.asList("core", "empire"));
		MetaCard recycling = dictionnary.resolve("recycling_station");
		MetaCard nuclearPlant = dictionnary.resolve("nuclear_plant");

		PlayerEmpire player = new PlayerEmpire(Empire.with(dictionnary.resolve("empire/basic")))
				.draft(Arrays.asList(new DraftableCard(1, recycling), new DraftableCard(2, recycling),
						new DraftableCard(3, recycling), new DraftableCard(4, nuclearPlant)));

		SimpleModule module = new WonderfullModuleBuilder(dictionnary).createModule();
		ObjectMapper om = new ObjectMapper().setSerializationInclusion(Include.NON_NULL).registerModule(module);
		ObjectWriter printer = om.writerWithDefaultPrettyPrinter();
		logger.debug(printer.writeValueAsString(player));

		logger.debug("Draft cards");
		player = player.draft(new ActionDraft(1), consumer);
		player = player.draft(new ActionDraft(2), consumer);

		logger.debug(printer.writeValueAsString(player.asDTO(false, false)));
		logger.debug(printer.writeValueAsString(player.asPlayerAction(false)));
		player = player.draft(new ActionDraft(3), consumer);
		player = player.draft(new ActionDraft(4), consumer);
		logger.debug(printer.writeValueAsString(player));

		// within draft
		player = om.readValue(printer.writeValueAsString(player), PlayerEmpire.class);

		logger.debug("To build phase");
		player = player.draftToPlanning();
		logger.debug(printer.writeValueAsString(player));

		// within planning
		player = om.readValue(printer.writeValueAsString(player), PlayerEmpire.class);

		DraftedCard drafted = player.getPlanning().getDrafteds().get(0);
		int draftedId = drafted.getId();
		logger.debug("To production");
		player = player.moveToProduction(new ActionMoveDraftedToProduction(draftedId), consumer);
		player = player.moveToProduction(new ActionMoveDraftedToProduction(4), consumer);
		logger.debug(printer.writeValueAsString(player));

		logger.debug("Recycle 1");
		player = player.recycleToProduction(new ActionRecycleDraftedToProduction(2, draftedId), consumer);
		logger.debug(printer.writeValueAsString(player));
		logger.debug("Recycle 2");
		player = player.recycleToProduction(new ActionRecycleDraftedToProduction(3, draftedId), consumer);
		logger.debug(printer.writeValueAsString(player));

		logger.debug("Production material");
		player = player.startProductionStep(Token.MATERIAL);
		logger.debug(printer.writeValueAsString(player));

		// within production
		player = om.readValue(printer.writeValueAsString(player), PlayerEmpire.class);

		logger.debug("Supremacy");
		player = player.supremacy(new ActionSupremacy(true), consumer);
		logger.debug(printer.writeValueAsString(player));

		logger.debug("Affect 1 material");
		player = player.affectToProduction(new ActionAffectToProduction(4, Map.of(0, Token.MATERIAL)), consumer);
		logger.debug(printer.writeValueAsString(player));
		logger.debug(printer.writeValueAsString(player.asDTO(false, false)));
		logger.debug(printer.writeValueAsString(player.asPlayerAction(false)));

		logger.debug("Affect 3 material");
		player = player.affectToProduction(
				new ActionAffectToProduction(4, Map.of(1, Token.MATERIAL, 2, Token.MATERIAL, 3, Token.MATERIAL)),
				consumer);
		logger.debug(printer.writeValueAsString(player));
		player = player.endProductionStep(consumer);

		logger.debug("Production energy");
		player = player.startProductionStep(Token.ENERGY);
		player = player.endProductionStep(consumer);
		logger.debug(printer.writeValueAsString(player));

		logger.debug("Production science");
		player = player.startProductionStep(Token.SCIENCE);
		logger.debug(printer.writeValueAsString(player));

		logger.debug("Affect 1 science");
		player = player.affectToProduction(new ActionAffectToProduction(4, Map.of(4, Token.SCIENCE)), consumer);
		player = player.endProductionStep(consumer);
		logger.debug(printer.writeValueAsString(player.asDTO(false, false)));

		logger.debug("Production energy");
		player = player.startProductionStep(Token.ENERGY);
		logger.debug(printer.writeValueAsString(player.asDTO(false, false)));
		logger.debug(printer.writeValueAsString(player.asPlayerAction(false)));

	}

}
