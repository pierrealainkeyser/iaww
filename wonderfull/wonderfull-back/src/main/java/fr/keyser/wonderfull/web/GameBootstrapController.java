package fr.keyser.wonderfull.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.keyser.fsm.InstanceId;
import fr.keyser.wonderfull.security.UserPrincipalRepository;
import fr.keyser.wonderfull.world.GameBootstrapConfiguration;
import fr.keyser.wonderfull.world.game.ActiveGame;
import fr.keyser.wonderfull.world.game.GameBootstraper;

@RestController
@RequestMapping("/game")
public class GameBootstrapController {

	private final GameBootstraper bootstraper;

	private final UserPrincipalRepository users;

	public GameBootstrapController(GameBootstraper bootstraper, UserPrincipalRepository users) {
		this.bootstraper = bootstraper;
		this.users = users;
	}

	@PostMapping("/bootstrap")
	public InstanceId bootstrap(@RequestBody GameBootstrapConfiguration bootstrap) {
		ActiveGame started = bootstraper.starts(users.createConfiguration(bootstrap));
		return started.getId();
	}

	@DeleteMapping("/{gameId}")
	public void stop(@PathVariable String gameId) {
		bootstraper.stop(new InstanceId(gameId));
	}

}
