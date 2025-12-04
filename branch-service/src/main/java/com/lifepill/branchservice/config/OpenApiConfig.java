package com.lifepill.branchservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8081}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");
        
        Server gatewayServer = new Server()
                .url("http://localhost:9191")
                .description("API Gateway Server");
        
        Contact contact = new Contact()
                .name("LifePill Team")
                .email("support@lifepill.com")
                .url("https://lifepill.com");
        
        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
        
        Info info = new Info()
                .title("LifePill Branch Service API")
                .version("1.0.0")
                .description("Branch and Employer Management Service for LifePill Platform")
                .contact(contact)
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(gatewayServer, localServer));
    }
}
