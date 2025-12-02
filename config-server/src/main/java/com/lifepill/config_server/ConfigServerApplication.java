package com.lifepill.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Centralized Configuration Server for LifePill Microservices.
 * 
 * This server provides externalized configuration for all microservices
 * in the LifePill ecosystem, enabling:
 * - Centralized configuration management
 * - Environment-specific configurations
 * - Dynamic configuration updates
 * - Encrypted property support
 * 
 * @author LifePill Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

}
