package com.lifepill.identityservice.config;

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
 * OpenAPI/Swagger configuration for Identity Service
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8085}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");
        
        Server gatewayServer = new Server()
                .url("http://localhost:9191/lifepill/v1/identity")
                .description("API Gateway Server");
        
        Contact contact = new Contact()
                .name("LifePill Team")
                .email("support@lifepill.com")
                .url("https://lifepill.com");
        
        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
        
        Info info = new Info()
                .title("LifePill Employee Identity Service API")
                .version("1.0.0")
                .description("Employee Authentication and Identity Management Service for LifePill Platform. " +
                        "This service handles employer registration, authentication (email/password and PIN), " +
                        "JWT token management, and employer profile management.")
                .contact(contact)
                .license(license);
        
        // Security scheme for JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("JWT Authentication")
                .description("Enter JWT token");
        
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, gatewayServer))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}
