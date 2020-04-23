package fr.keyser.wonderfull.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import fr.keyser.wonderfull.security.event.UserPrincipalConnectedEvent;
import fr.keyser.wonderfull.security.event.UserPrincipalDisconnectedEvent;
import fr.keyser.wonderfull.security.event.UserPrincipalEvent;

public class ConnectedUserService {

	private final ConnectedUserRepository repository;

	private final SimpMessageSendingOperations sendingOperations;

	public ConnectedUserService(ConnectedUserRepository repository, SimpMessageSendingOperations sendingOperations) {
		this.repository = repository;
		this.sendingOperations = sendingOperations;
	}

	public List<UserPrincipal> getAllConnectedUsers() {
		return repository.getAllConnectedUsers();
	}

	@EventListener
	public void userConnected(UserPrincipalConnectedEvent event) {
		broadcast(event, "connected");
	}

	@EventListener
	public void userDisonnected(UserPrincipalDisconnectedEvent event) {
		broadcast(event, "disconnected");
	}

	private void broadcast(UserPrincipalEvent event, String type) {
		Map<String, Serializable> payload = Map.of("type", type, "user", event.getSource());
		sendingOperations.convertAndSend("/topic/users", payload);
	}

}
