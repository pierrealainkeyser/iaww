package fr.keyser.wonderfull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.access.expression.DefaultMessageSecurityExpressionHandler;

import fr.keyser.wonderfull.security.ActiveGameRepositoryPermissionEvaluator;
import fr.keyser.wonderfull.world.game.ActiveGameMapRepository;

@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Bean
	public DefaultMessageSecurityExpressionHandler<Object> messageSecurityExpressionHandler(
			ActiveGameMapRepository repository) {
		DefaultMessageSecurityExpressionHandler<Object> handler = new DefaultMessageSecurityExpressionHandler<>();
		handler.setPermissionEvaluator(new ActiveGameRepositoryPermissionEvaluator(repository));
		return handler;
	}

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {

		messages.simpSubscribeDestMatchers("/app/game/{externalId}/**")
				.access("hasPermission(#externalId,'SUBSCRIBE')");
		messages.simpSubscribeDestMatchers("/user/game/{externalId}/**")
				.access("hasPermission(#externalId,'SUBSCRIBE')");

		messages.simpMessageDestMatchers("/app/game/{externalId}/**").access("hasPermission(#externalId,'MESSAGE')");

	}
}