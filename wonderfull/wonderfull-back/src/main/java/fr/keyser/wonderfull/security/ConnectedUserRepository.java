package fr.keyser.wonderfull.security;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import fr.keyser.wonderfull.security.event.UserPrincipalConnectedEvent;
import fr.keyser.wonderfull.security.event.UserPrincipalDisconnectedEvent;

public class ConnectedUserRepository {

	private final ConcurrentMap<String, Integer> connected = new ConcurrentHashMap<>();

	private final UserPrincipalRepository repository;

	private final ApplicationEventPublisher publisher;

	public ConnectedUserRepository(UserPrincipalRepository repository, ApplicationEventPublisher publisher) {
		this.repository = repository;
		this.publisher = publisher;
	}

	public List<UserPrincipal> getAllConnectedUsers() {
		return repository.getByIds(connected.keySet());
	}

	@EventListener
	public void sessionConnect(SessionConnectEvent event) {
		Principal user = event.getUser();
		if (user != null) {
			connected.compute(user.getName(), (k, old) -> {
				if (old == null)
					old = 0;

				Optional<UserPrincipal> found = repository.getById(k);
				found.ifPresent(up -> publisher.publishEvent(new UserPrincipalConnectedEvent(up)));

				// add user
				return old + 1;
			});
		}
	}

	@EventListener
	public void sessionDisconnect(SessionDisconnectEvent event) {
		Principal user = event.getUser();
		if (user != null) {
			connected.compute(user.getName(), (k, old) -> {
				if (old != null) {
					old -= 1;

					if (old <= 0) {
						old = null;
						Optional<UserPrincipal> found = repository.getById(k);
						found.ifPresent(up -> publisher.publishEvent(new UserPrincipalDisconnectedEvent(up)));
					}
				}
				return old;
			});
		}
	}
}
