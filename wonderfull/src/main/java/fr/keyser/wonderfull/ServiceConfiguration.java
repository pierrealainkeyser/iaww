package fr.keyser.wonderfull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import fr.keyser.wonderfull.world.game.ActiveGameRepository;
import fr.keyser.wonderfull.world.game.GameDTOBuilder;
import fr.keyser.wonderfull.world.game.InGameService;

@Configuration
public class ServiceConfiguration {

	@Bean
	public InGameService inGameService(ActiveGameRepository repository, SimpMessageSendingOperations sendingOperations,
			GameDTOBuilder builder) {
		return new InGameService(repository, sendingOperations, builder);
	}

}
