package fr.keyser.wonderfull.security;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import fr.keyser.wonderfull.world.EmpireConfiguration;
import fr.keyser.wonderfull.world.GameBootstrapConfiguration;
import fr.keyser.wonderfull.world.GameBootstrapConfiguration.UserConfiguration;
import fr.keyser.wonderfull.world.GameConfiguration;

public class UserPrincipalRepository {

	private static final Logger logger = LoggerFactory.getLogger(UserPrincipalRepository.class);

	private final JdbcOperations jdbc;

	public UserPrincipalRepository(JdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Transactional
	public void add(UserPrincipal user) {
		jdbc.update("insert into user(uid, name) select ?,? where not exists (select * from user where uid=?)",
				user.getName(), user.getLabel(), user.getName());

		logger.info("Adding user : {}", user);
	}

	public GameConfiguration createConfiguration(GameBootstrapConfiguration conf, ProviderPrincipal convert) {

		logger.info("Creating game configuration : {} for {}", conf, convert);

		Instant createdAt = Instant.now();
		List<UserConfiguration> users = conf.getUsers();
		List<UserPrincipal> byIds = getByIds(
				users.stream().map(UserConfiguration::getUid).collect(Collectors.toList()));

		List<EmpireConfiguration> empires = users.stream().map(u -> {
			Optional<UserPrincipal> first = byIds.stream().filter(us -> u.getUid().equals(us.getName())).findFirst();
			UserPrincipal user = first.get();
			String empire = Optional.ofNullable(u.getEmpire()).orElse("basic");

			return new EmpireConfiguration(user, UUID.randomUUID().toString(), empire);
		}).collect(Collectors.toList());

		Collections.shuffle(empires);

		List<String> dictionaries = conf.getDictionaries();
		if (dictionaries == null)
			dictionaries = Arrays.asList("core", "empire");
		return new GameConfiguration(convert.getName(), dictionaries, conf.getStartingEmpire(), empires, createdAt);

	}

	public Optional<UserPrincipal> getById(String ids) {
		return getByIds(Arrays.asList(ids)).stream().findFirst();
	}

	public List<UserPrincipal> getByIds(Collection<String> ids) {
		String params = String.join(", ", Collections.nCopies(ids.size(), "?"));
		String sql = "select uid,name from user where uid in (" + params + ")";
		return jdbc.query(sql, ids.toArray(), (rs, index) -> {
			return new UserPrincipal(rs.getString("uid"), rs.getString("name"));
		});
	}

}
