package fr.keyser.wonderfull;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;

import fr.keyser.wonderfull.world.MetaCardDictionnary;
import fr.keyser.wonderfull.world.MetaCardDictionnaryLoader;
import fr.keyser.wonderfull.world.WonderfullModuleBuilder;

@Configuration
public class JacksonConfiguration {

	@Bean
	public SimpleModule wonderfullModel(MetaCardDictionnaryLoader loader) {
		MetaCardDictionnary dictionnary = loader.load(Arrays.asList("core", "empire", "ks0", "wop"));
		return new WonderfullModuleBuilder(dictionnary).createModule();
	}

}
