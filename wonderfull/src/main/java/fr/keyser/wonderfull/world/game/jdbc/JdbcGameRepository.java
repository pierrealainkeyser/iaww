package fr.keyser.wonderfull.world.game.jdbc;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.keyser.fsm.impl.AutomatsMemento;
import fr.keyser.wonderfull.world.game.ActiveGame;
import fr.keyser.wonderfull.world.game.GameInfo;
import fr.keyser.wonderfull.world.game.GameLoader;

public class JdbcGameRepository {

	private final GameLoader loader;

	private final ObjectMapper objectMapper;

	private final JdbcOperations jdbc;

	public JdbcGameRepository(GameLoader loader, ObjectMapper objectMapper, JdbcOperations jdbc) {
		this.loader = loader;
		this.objectMapper = objectMapper;
		this.jdbc = jdbc;
	}

	@Transactional
	public void create(ActiveGame game) {

		try {
			String content = extractContent(game);
			jdbc.update("insert into active_game(uuid,content) values(?,?)", game.getId().getId(), content);

		} catch (JsonProcessingException e) {
			throw wrap(e);
		}
	}

	private String extractContent(ActiveGame game) throws JsonProcessingException {
		return objectMapper.writeValueAsString(game.getAutomats().memento());
	}

	@Transactional
	public void delete(ActiveGame game) {
		jdbc.update("delete from active_game where uuid=?", game.getId().getId());
	}

	@Transactional(readOnly = true)
	public List<ActiveGame> reload() {
		return jdbc.query("select content from active_game", (rs, index) -> {
			String content = rs.getString("content");

			AutomatsMemento memento;
			try {
				memento = objectMapper.readValue(content, AutomatsMemento.class).map(payload -> {
					if (payload != null) {
						return objectMapper.convertValue(payload, GameInfo.class);
					}
					return null;
				});
			} catch (JsonProcessingException e) {
				throw wrap(e);
			}

			return loader.reload(memento);

		});
	}

	private RuntimeException wrap(JsonProcessingException e) {
		return new RuntimeException(e);
	}

	@Transactional
	public void update(ActiveGame game) {
		try {
			String content = extractContent(game);
			jdbc.update("update active_game set content=? where uuid=?", content, game.getId().getId());

		} catch (JsonProcessingException e) {
			throw wrap(e);
		}
	}
}
