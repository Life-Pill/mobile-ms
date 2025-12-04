package com.lifepill.inventoryservice.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * Request interceptor to add common headers
     * Can be used to pass trace IDs, authentication tokens, etc.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Service-Name", "INVENTORY-SERVICE");
            requestTemplate.header("Content-Type", "application/json");
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
