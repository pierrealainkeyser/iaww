package fr.keyser.wonderfull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.keyser.wonderfull.world.game.ActiveGameMapRepository;
import fr.keyser.wonderfull.world.game.GameLoader;
import fr.keyser.wonderfull.world.game.jdbc.JdbcBackedActiveGameRepository;
import fr.keyser.wonderfull.world.game.jdbc.JdbcGameRepository;

@Configuration
public class JdbcConfiguration {

	@Bean
	public JdbcGameRepository jdbcGameRepository(GameLoader loader, ObjectMapper objectMapper, JdbcOperations jdbc) {
		return new JdbcGameRepository(loader, objectMapper, jdbc);
	}

	@Bean
	public JdbcBackedActiveGameRepository jdbcBackedActiveGameRepository(ActiveGameMapRepository repository,
			JdbcGameRepository storage) {
		return new JdbcBackedActiveGameRepository(repository, storage);
	}
}
