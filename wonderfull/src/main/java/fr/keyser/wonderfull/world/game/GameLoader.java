package fr.keyser.wonderfull.world.game;

import fr.keyser.fsm.impl.Automats;
import fr.keyser.fsm.impl.AutomatsMemento;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;

public class GameLoader {

	private final GameAutomatsBuilder builder;

	private final MetaCardDictionnaryLoader loader;

	public GameLoader(GameAutomatsBuilder builder, MetaCardDictionnaryLoader loader) {
		this.builder = builder;
		this.loader = loader;
	}

	public ActiveGame reload(AutomatsMemento memento) {

		Automats<GameInfo> automats = builder.build(memento.getId(), memento.getMementos().size() - 1);

		automats.reload(memento.map(payload -> {
			if (payload instanceof GameInfo) {
				return ((GameInfo) payload).reloadDictionnary(loader);
			}
			return null;
		}));

		return new ActiveGame(automats);
	}

}
