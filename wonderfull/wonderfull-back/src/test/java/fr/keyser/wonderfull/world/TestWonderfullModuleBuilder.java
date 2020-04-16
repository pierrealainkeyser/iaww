package fr.keyser.wonderfull.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import fr.keyser.wonderfull.world.BuildedCard;
import fr.keyser.wonderfull.world.DraftableCard;
import fr.keyser.wonderfull.world.DraftedCard;
import fr.keyser.wonderfull.world.InProductionCard;
import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.ProductionSlot;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.MetaCard;
import fr.keyser.wonderfull.world.WonderfullModuleBuilder;

public class TestWonderfullModuleBuilder {

	private static final Logger logger = LoggerFactory.getLogger(TestWonderfullModuleBuilder.class);

	@Test
	void draftable() throws JsonProcessingException {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("core");

		SimpleModule module = new WonderfullModuleBuilder(dictionnary).createModule();
		ObjectMapper om = new ObjectMapper().registerModule(module);

		MetaCard recycling = dictionnary.resolve("recycling_station");
		DraftableCard input = new DraftableCard(1, recycling);

		String str = om.writeValueAsString(input);
		logger.debug(str);
		DraftableCard read = om.readValue(str, DraftableCard.class);
		assertThat(read.getId()).isEqualTo(input.getId());
		assertThat(read.meta).isSameAs(input.meta);

	}

	@Test
	void drafted() throws JsonProcessingException {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("core");

		SimpleModule module = new WonderfullModuleBuilder(dictionnary).createModule();
		ObjectMapper om = new ObjectMapper().registerModule(module);

		MetaCard recycling = dictionnary.resolve("recycling_station");
		DraftedCard input = new DraftedCard(1, recycling);

		String str = om.writeValueAsString(input);
		logger.debug(str);
		DraftedCard read = om.readValue(str, DraftedCard.class);
		assertThat(read.getId()).isEqualTo(input.getId());
		assertThat(read.meta).isSameAs(input.meta);

	}

	@Test
	void builded() throws JsonProcessingException {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("core");

		SimpleModule module = new WonderfullModuleBuilder(dictionnary).createModule();
		ObjectMapper om = new ObjectMapper().registerModule(module);

		MetaCard recycling = dictionnary.resolve("recycling_station");
		BuildedCard input = new BuildedCard(1, recycling);

		String str = om.writeValueAsString(input);
		logger.debug(str);
		BuildedCard read = om.readValue(str, BuildedCard.class);
		assertThat(read.getId()).isEqualTo(input.getId());
		assertThat(read.meta).isSameAs(input.meta);

	}

	@Test
	void inProduction() throws JsonProcessingException {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("core");

		SimpleModule module = new WonderfullModuleBuilder(dictionnary).createModule();
		ObjectMapper om = new ObjectMapper().registerModule(module);

		MetaCard recycling = dictionnary.resolve("recycling_station");
		InProductionCard input = new InProductionCard(1, recycling, Arrays.asList(new ProductionSlot(Token.MATERIAL)));

		String str = om.writeValueAsString(input);
		logger.debug(str);
		InProductionCard read = om.readValue(str, InProductionCard.class);
		assertThat(read.getId()).isEqualTo(input.getId());
		assertThat(read.meta).isSameAs(input.meta);
		assertThat(read.slots).isEqualTo(input.slots);

	}
}
