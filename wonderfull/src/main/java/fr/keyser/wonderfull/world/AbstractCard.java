package fr.keyser.wonderfull.world;

import java.util.List;

public class AbstractCard {

	public static int findIndex(List<? extends AbstractCard> source, int targetId) {
		int productionIndex = -1;
		int i = 0;
		int size = source.size();
		while (productionIndex < 0 && i < size) {
			AbstractCard p = source.get(i);
			if (targetId == p.getId()) {
				productionIndex = i;
			} else {
				++i;
			}
		}

		if (productionIndex < 0)
			throw new NoSuchCardException(targetId);
		return productionIndex;
	}

	protected final int id;

	protected final MetaCard meta;

	public AbstractCard(int id, MetaCard meta) {
		this.id = id;
		this.meta = meta;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return meta.getName() + "#" + id;
	}

	public String getName() {
		return meta.getName();
	}
}
