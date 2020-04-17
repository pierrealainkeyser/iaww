package fr.keyser.wonderfull.security;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.annotation.Transactional;

public class UserPrincipalRepository {

	private final JdbcOperations jdbc;

	public UserPrincipalRepository(JdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Transactional
	public void add(UserPrincipal user) {
		jdbc.update("insert into user(uid, name) select ?,? where not exists (select * from user where uid=?)",
				user.getUid(), user.getName(), user.getUid());
	}

	public List<UserPrincipal> listAll() {
		return jdbc.query("select uid,name from user", (rs, index) -> {
			return new UserPrincipal(rs.getString("name"), rs.getString("uid"));
		});
	}

}
