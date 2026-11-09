package com.AnimeProject.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	WebMvcConfigurer corsConfigurer(@Value("${app.cors.allowed-origins}") String origins) {
		String[] allowedOrigins = Arrays.stream(origins.split(","))
				.map(String::trim)
				.filter(origin -> !origin.isEmpty())
				.toArray(String[]::new);

		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/animes/**")
						.allowedOrigins(allowedOrigins)
						.allowedMethods("GET", "POST", "OPTIONS")
						.allowedHeaders("*");
			}
		};
	}

}
