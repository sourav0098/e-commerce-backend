package com.quickpik.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI myOpenAPI() {
		Server devServer = new Server();
		devServer.setUrl("localhost:8080");
		devServer.setDescription("Server URL in Development environment");

		Contact contact = new Contact();
		contact.setEmail("sourav09dev@gmail.com");
		contact.setName("Sourav Choudhary");

		Info info = new Info().title("QuickPik: Ecommerce API").version("1.0").contact(contact)
				.description("An API for an electronics e-commerce website");

		return new OpenAPI().info(info).servers(List.of(devServer));
	}

}
