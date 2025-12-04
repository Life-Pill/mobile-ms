package com.lifepill.identityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class for the LifePill Employee Identity Service.
 * This service handles authentication and employee management for the POS system.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EmployeeIdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeIdentityServiceApplication.class, args);
    }
}
