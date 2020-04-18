package fr.keyser.wonderfull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.trace.http.HttpTraceEndpoint;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class MainWonderfull {

	public static void main(String[] args) {
		SpringApplication.run(MainWonderfull.class, args);
	}

	@ConditionalOnAvailableEndpoint(endpoint = HttpTraceEndpoint.class)
	@Bean
	public InMemoryHttpTraceRepository inMemoryHttpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}
}
