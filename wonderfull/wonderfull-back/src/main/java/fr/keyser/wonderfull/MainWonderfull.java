package fr.keyser.wonderfull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class MainWonderfull {

	public static void main(String[] args) {
		SpringApplication.run(MainWonderfull.class, args);
	}

}
