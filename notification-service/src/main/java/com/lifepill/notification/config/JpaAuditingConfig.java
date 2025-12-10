package com.lifepill.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration to enable JPA auditing for automatic timestamp fields.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
