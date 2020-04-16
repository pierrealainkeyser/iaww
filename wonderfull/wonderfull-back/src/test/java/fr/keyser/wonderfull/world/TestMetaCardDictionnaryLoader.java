package fr.keyser.wonderfull.world;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.Production;
import fr.keyser.wonderfull.world.Token;
import fr.keyser.wonderfull.world.Value;
import fr.keyser.wonderfull.world.MetaCard;

public class TestMetaCardDictionnaryLoader {

	@Test
	void load() {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("core");

		MetaCard recycling = dictionnary.resolve("recycling_station");
		assertThat(recycling.getBonus()).isNull();
		assertThat(recycling.getScoring()).isNull();
		assertThat(recycling.getCost()).isEqualTo(Token.MATERIAL.token(2));
		assertThat(recycling.getProduce()).isEqualTo(new Production(Map.of(Token.MATERIAL, Value.constant(2))));
		assertThat(recycling.getRecycle()).isEqualTo(Token.MATERIAL);
		assertThat(recycling.getType()).isEqualTo(Token.MATERIAL);
		assertThat(recycling.getOccurence()).isEqualTo(7);

	}

}
