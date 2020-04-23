package fr.keyser.wonderfull.web;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.keyser.wonderfull.security.ProviderPrincipal;
import fr.keyser.wonderfull.security.ProviderPrincipalConverter;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final ProviderPrincipalConverter converter;

	private final Iterable<ClientRegistration> clientRegistrations;

	private final CsrfTokenRepository csrfTokenRepository;

	public AuthController(ProviderPrincipalConverter converter, Iterable<ClientRegistration> clientRegistrations,
			CsrfTokenRepository csrfTokenRepository) {
		this.converter = converter;
		this.clientRegistrations = clientRegistrations;
		this.csrfTokenRepository = csrfTokenRepository;
	}

	@GetMapping("/principal")
	public ResponseEntity<ProviderPrincipal> principal(HttpServletRequest request, Principal principal) {
		BodyBuilder status = ResponseEntity.status(HttpStatus.OK);
		status.headers(h -> {
			h.add("Access-Control-Expose-Headers", "X-CSRF-TOKEN");
			h.add("X-CSRF-TOKEN", csrfTokenRepository.loadToken(request).getToken());
		});
		return status.body(converter.convert(principal));
	}

	@GetMapping("/clients")
	public ResponseEntity<Map<String, String>> clients() {
		Map<String, String> out = new LinkedHashMap<>();
		clientRegistrations.forEach(c -> out.put(c.getClientName(), "oauth2/authorization/" + c.getRegistrationId()));
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(out);

	}

}
