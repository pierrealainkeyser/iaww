package fr.keyser.wonderfull.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import fr.keyser.wonderfull.world.UnresolvedGameIdException;
import fr.keyser.wonderfull.world.game.ActiveGameRepository;
import fr.keyser.wonderfull.world.game.InGameId;
import fr.keyser.wonderfull.world.game.ResolvedGame;

public class ActiveGameRepositoryPermissionEvaluator implements PermissionEvaluator {

	private final ActiveGameRepository repository;

	public ActiveGameRepositoryPermissionEvaluator(ActiveGameRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

		String externalId = targetDomainObject.toString();
		try {
			ResolvedGame resolved = repository.findByExternal(externalId);

			int myself = resolved.getMyself();
			InGameId player = resolved.getPlayers().get(myself);
			String username = player.getUser().getName();
			String currentName = authentication.getName();

			return username.equals(currentName);

		} catch (UnresolvedGameIdException e) {
			// noop
		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {

		return hasPermission(authentication, targetId, permission);
	}

}
