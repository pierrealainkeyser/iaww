package fr.keyser.wonderfull.security;

import java.security.Principal;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class ProviderPrincipalConverter {

	private final UserPrincipalRepository repository;

	public ProviderPrincipalConverter(UserPrincipalRepository repository) {
		this.repository = repository;
	}

	public ProviderPrincipal convert(Principal principal) {
		if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
			String client = token.getAuthorizedClientRegistrationId();
			return convert(client, token.getPrincipal());
		}

		if (principal instanceof OAuth2LoginAuthenticationToken) {
			OAuth2LoginAuthenticationToken token = (OAuth2LoginAuthenticationToken) principal;
			String client = token.getClientRegistration().getRegistrationId();
			return convert(client, token.getPrincipal());
		}

		if (principal instanceof ProviderPrincipal) {
			return (ProviderPrincipal) principal;
		}

		return null;
	}

	private ProviderPrincipal convert(String client, OAuth2User user) {
		String id = null;
		if ("google".equals(client)) {
			id = user.getAttribute("sub");
		} else if ("github".equals(client)) {
			id = user.getAttribute("login");
		}
		return new ProviderPrincipal(id + "@" + client, user.getAttribute("name"), client);

	}

	@EventListener
	public void eventListener(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		ProviderPrincipal pp = convert(authentication);
		if (pp != null)
			repository.add(pp);
	}
}
