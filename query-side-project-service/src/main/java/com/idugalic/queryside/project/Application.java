package com.idugalic.queryside.project;

import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
	public static void main(final String... args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAware<String>() {
			@Override
			public Optional<String> getCurrentAuditor() {
				final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

				if (authentication == null || !authentication.isAuthenticated()) {
					return Optional.empty();
				}

				return Optional.of(authentication.getName());
			}
		};
	}

}
