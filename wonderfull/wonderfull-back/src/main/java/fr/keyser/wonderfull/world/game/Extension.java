package fr.keyser.wonderfull.world.game;

import java.util.Collection;

public class Extension {

	public static boolean containsWarOrPeace(Collection<String> dictionnary) {
		return dictionnary.stream().anyMatch("wop"::equals);
	}
}
