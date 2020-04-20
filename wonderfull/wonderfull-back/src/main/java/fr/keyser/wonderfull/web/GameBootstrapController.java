package fr.keyser.wonderfull.web;

import java.security.Principal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.security.ProviderPrincipal;
import fr.keyser.wonderfull.security.ProviderPrincipalConverter;
import fr.keyser.wonderfull.security.UserPrincipalRepository;
import fr.keyser.wonderfull.world.GameBootstrapConfiguration;
import fr.keyser.wonderfull.world.PlayerGameDescription;
import fr.keyser.wonderfull.world.game.ActiveGame;
import fr.keyser.wonderfull.world.game.GameBootstraper;

@RestController
@RequestMapping("/game")
public class GameBootstrapController {

	private final GameBootstraper bootstraper;

	private final UserPrincipalRepository users;

	private final ProviderPrincipalConverter converter;

	public GameBootstrapController(GameBootstraper bootstraper, UserPrincipalRepository users,
			ProviderPrincipalConverter converter) {
		this.bootstraper = bootstraper;
		this.users = users;
		this.converter = converter;
	}

	@PostMapping("/bootstrap")
	public PlayerGameDescription bootstrap(@RequestBody GameBootstrapConfiguration bootstrap, Principal principal) {

		ProviderPrincipal convert = converter.convert(principal);

		ActiveGame started = bootstraper.starts(users.createConfiguration(bootstrap, convert));
		return started.asDescription(convert.getName());
	}

	@DeleteMapping("/{gameId}")
	public void stop(@PathVariable String gameId, Principal principal) {

		ProviderPrincipal convert = converter.convert(principal);

		bootstraper.stop(new InstanceId(gameId), convert.getName());
	}

}
