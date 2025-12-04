package com.lifepill.possystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration with JWT Bearer token authentication.
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Value("${server.port:8086}")
    private String serverPort;

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");

        Server gatewayServer = new Server()
                .url("http://localhost:9191")
                .description("API Gateway Server");

        return new OpenAPI()
                .info(new Info()
                        .title("LifePill POS System API")
                        .description("REST API documentation for LifePill Pharmacy POS System. " +
                                "Handles orders, payments, and POS operations.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("LifePill Team")
                                .email("support@lifepill.com")
                                .url("https://lifepill.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(localServer, gatewayServer))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token. Get token from Identity Service /lifepill/v1/identity/auth/authenticate endpoint")));
    }
}
