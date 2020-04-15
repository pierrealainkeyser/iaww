package fr.keyser.wonderfull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import fr.keyser.wonderfull.world.game.jdbc.JdbcBackedActiveGameRepository;
import fr.keyser.wonderfull.world.game.simp.SimpGameRepository;

@Configuration
public class SimpConfiguration {

	@Bean
	@Primary
	public SimpGameRepository simpGameRepository(JdbcBackedActiveGameRepository repository,
			SimpMessageSendingOperations simp) {
		return new SimpGameRepository(repository, simp);
	}
}
