package fr.keyser.wonderfull;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import fr.keyser.wonderfull.security.ProviderPrincipalConverter;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	private final ProviderPrincipalConverter converter;

	public WebSocketConfiguration(ProviderPrincipalConverter converter) {
		this.converter = converter;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes("/app");
		config.setUserDestinationPrefix("/user");
		
		// workarround for
		// https://github.com/spring-projects/spring-framework/issues/23104
		config.setPreservePublishOrder(true);
		config.setCacheLimit(0);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setAllowedOrigins("*").setHandshakeHandler(new DefaultHandshakeHandler() {
			@Override
			protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
					Map<String, Object> attributes) {
				return converter.convert(request.getPrincipal());
			}
		});
	}
}