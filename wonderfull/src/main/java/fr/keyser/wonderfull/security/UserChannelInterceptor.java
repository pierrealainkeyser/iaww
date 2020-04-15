package fr.keyser.wonderfull.security;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserChannelInterceptor implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (StompCommand.CONNECT == accessor.getCommand()) {
			List<String> user = accessor.getNativeHeader("X-User");
			if (user != null && !user.isEmpty()) {
				accessor.setUser(new UsernamePasswordAuthenticationToken(user.get(0), "N/A"));
			}
		}

		return ChannelInterceptor.super.preSend(message, channel);
	}
}
