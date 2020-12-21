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
	
	@Test
	void loadKs() {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("ks0");
		
		MetaCard terraformation = dictionnary.resolve("terraformation");
		assertThat(terraformation.getBonus()).isNull();
	}
	
	@Test
	void loadAsc() {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("asc");
		
		MetaCard terraformation = dictionnary.resolve("border_patrol");
		assertThat(terraformation.getBonus()).isEqualTo(Token.GENERAL.one());
	}
	
	@Test
	void loadWop() {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("wop");
		
		MetaCard terraformation = dictionnary.resolve("secret_forces");
		assertThat(terraformation.getBonus()).isNull();
	}
	
	@Test
	void loadEmpires() {
		MetaCardDictionnaryLoader loader = new MetaCardDictionnaryLoader();
		MetaCardDictionnary dictionnary = loader.load("empire");
		
		MetaCard terraformation = dictionnary.resolve("empire/noram_F");
		assertThat(terraformation.getBonus()).isNull();
	}

}
