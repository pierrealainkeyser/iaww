package fr.keyser.wonderfull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.game.ActiveGameMapRepository;
import fr.keyser.wonderfull.world.game.ActiveGameRepository;
import fr.keyser.wonderfull.world.game.GameAutomatsBuilder;
import fr.keyser.wonderfull.world.game.GameBootstraper;
import fr.keyser.wonderfull.world.game.GameDTOBuilder;
import fr.keyser.wonderfull.world.game.GameLoader;

@Configuration
public class CoreConfiguration {

	@Bean
	public GameAutomatsBuilder gameAutomatsBuilder() {
		return new GameAutomatsBuilder();
	}

	@Bean
	public MetaCardDictionnaryLoader metaCardDictionnaryLoader() {
		return new MetaCardDictionnaryLoader();
	}

	@Bean
	public ActiveGameMapRepository activeGameRepository() {
		return new ActiveGameMapRepository();
	}

	@Bean
	public GameDTOBuilder gameDTOBuilder(MetaCardDictionnaryLoader loader) {
		return new GameDTOBuilder(loader);
	}

	@Bean
	public GameLoader gameLoader(GameAutomatsBuilder builder, MetaCardDictionnaryLoader loader) {
		return new GameLoader(builder, loader);
	}

	@Bean
	public GameBootstraper gameBootstraper(GameAutomatsBuilder builder, MetaCardDictionnaryLoader loader,
			ActiveGameRepository repository) {
		return new GameBootstraper(builder, loader, repository);
	}
}
