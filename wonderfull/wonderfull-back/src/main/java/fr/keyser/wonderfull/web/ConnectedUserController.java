package fr.keyser.wonderfull.web;

import java.util.Map;

import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import fr.keyser.wonderfull.security.ConnectedUserService;

@Controller
public class ConnectedUserController {

	private final ConnectedUserService userService;

	public ConnectedUserController(ConnectedUserService userService) {
		this.userService = userService;
	}

	@SubscribeMapping("/users")
	public Map<String, Object> connectedUsers() {
		return Map.of("type", "all", "users", userService.getAllConnectedUsers());
	}
}
