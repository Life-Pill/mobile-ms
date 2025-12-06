package com.lifepill.branchservice.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Feign Client Configuration
 * Configures logging, retry, and error handling for Feign clients
 */
@Configuration
public class FeignClientConfig {

    /**
     * Enable full logging for Feign clients in development
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Configure retry mechanism for failed requests
     * Retries 3 times with 1 second intervals
     */
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 3);
    }

    /**
     * Request interceptor to propagate auth headers and add common headers.
     * Propagates Authorization header to downstream services.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Service-Name", "BRANCH-SERVICE");
            requestTemplate.header("Content-Type", "application/json");
            
            // Propagate auth headers from the incoming request
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // Forward Authorization header
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null) {
                    requestTemplate.header("Authorization", authHeader);
                }
                
                // Forward X-Auth headers from API Gateway
                String authUser = request.getHeader("X-Auth-User");
                if (authUser != null) {
                    requestTemplate.header("X-Auth-User", authUser);
                }
                
                String authRoles = request.getHeader("X-Auth-Roles");
                if (authRoles != null) {
                    requestTemplate.header("X-Auth-Roles", authRoles);
                }
                
                String authValidated = request.getHeader("X-Auth-Validated");
                if (authValidated != null) {
                    requestTemplate.header("X-Auth-Validated", authValidated);
                }
            }
        };
    }

    /**
     * Custom error decoder for handling Feign errors
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * Custom error decoder implementation
     */
    public static class FeignErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            if (response.status() == 404) {
                return new RuntimeException("Resource not found in remote service: " + methodKey);
            }
            if (response.status() >= 500) {
                return new RuntimeException("Remote service error: " + methodKey + " - Status: " + response.status());
            }
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
