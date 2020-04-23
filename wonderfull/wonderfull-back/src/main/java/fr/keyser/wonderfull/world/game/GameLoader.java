package fr.keyser.wonderfull.world.game;

import java.util.List;

import fr.keyser.fsm.impl.Automats;
import fr.keyser.fsm.impl.AutomatsMemento;
import fr.keyser.fsm.impl.InstanceMemento;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;

public class GameLoader {

	private final GameAutomatsBuilder builder;

	private final MetaCardDictionnaryLoader loader;

	public GameLoader(GameAutomatsBuilder builder, MetaCardDictionnaryLoader loader) {
		this.builder = builder;
		this.loader = loader;
	}

	public ActiveGame reload(AutomatsMemento memento) {

		List<InstanceMemento> mementos = memento.getMementos();
		GameInfo gi = (GameInfo) mementos.get(0).getPayload();

		boolean wop = Extension.containsWarOrPeace(gi.getGame().getConfiguration().getDictionaries());
		Automats<GameInfo> automats = builder.build(memento.getId(), mementos.size() - 1, wop);

		automats.reload(memento.map(payload -> {
			if (payload instanceof GameInfo) {
				return ((GameInfo) payload).reloadDictionnary(loader);
			}
			return null;
		}));

		return new ActiveGame(automats);
	}

}
