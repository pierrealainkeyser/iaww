package fr.keyser.wonderfull.world;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MetaCardDictionnaryLoader {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final Map<String, MetaCardDictionnary> dictionnaries = new ConcurrentHashMap<>();

	private final Map<String, MetaCardDictionnary> composite = new ConcurrentHashMap<>();

	public MetaCardDictionnary load(List<String> names) {
		String key = new TreeSet<>(names).stream().collect(Collectors.joining("+"));

		return composite.compute(key, (k, old) -> {
			if (old != null)
				return old;

			MetaCardDictionnary dict = new MetaCardDictionnary(Collections.emptyList());
			for (String name : names) {
				dict = dict.merge(load(name));
			}
			return dict;
		});

	}

	public MetaCardDictionnary load(String name) {

		return composite.compute(name, (k, old) -> {
			if (old != null)
				return old;

			URL url = MetaCardDictionnaryLoader.class.getResource("/meta/" + name + ".json");
			try {
				MetaCardDictionnary dict = objectMapper.readValue(url, MetaCardDictionnary.class);
				dict.setSet(name);
				dictionnaries.put(name, dict);
				return dict;
			} catch (IOException ioe) {
				throw new WonderfullIOException(ioe);
			}
		});
	}
}
