package fr.keyser.wonderfull.world;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class WonderfullModuleBuilder {

	private final MetaCardDictionnary dictionnary;

	public WonderfullModuleBuilder(MetaCardDictionnary dictionnary) {
		this.dictionnary = dictionnary;
	}

	public SimpleModule createModule() {
		return new SimpleModule("cards") //
				.addDeserializer(DraftedCard.class,
						new ProtoCardDeserializer<>(DraftedCard.class, DraftedCard::new, dictionnary))
				.addDeserializer(DraftableCard.class,
						new ProtoCardDeserializer<>(DraftableCard.class, DraftableCard::new, dictionnary))
				.addDeserializer(AbstractCard.class,
						new ProtoCardDeserializer<>(AbstractCard.class, AbstractCard::new, dictionnary))
				.addDeserializer(BuildedCard.class,
						new ProtoCardDeserializer<>(BuildedCard.class, BuildedCard::new, dictionnary))
				.addDeserializer(InProductionCard.class, new InProductionCardDeserializer(dictionnary));

	}

}
