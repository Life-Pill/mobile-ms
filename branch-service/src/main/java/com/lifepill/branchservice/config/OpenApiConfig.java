package com.lifepill.branchservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAPI/Swagger configuration
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${swagger.server.url:}")
    private String swaggerServerUrl;
    
    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();
        
        // Add configured server URL first if provided
        if (swaggerServerUrl != null && !swaggerServerUrl.isEmpty()) {
            servers.add(new Server()
                    .url(swaggerServerUrl)
                    .description("Primary Server"));
        }
        
        servers.add(new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server"));
        
        servers.add(new Server()
                .url("http://localhost:9191")
                .description("API Gateway Server"));
        
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
                .servers(servers)
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .name("bearerAuth")
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
