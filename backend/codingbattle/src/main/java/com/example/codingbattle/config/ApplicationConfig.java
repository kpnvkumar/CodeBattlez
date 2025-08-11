// File: src/main/java/com/codingbattle/config/ApplicationConfig.java
package com.example.codingbattle.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import jakarta.annotation.PostConstruct;
@Configuration
@EnableMongoAuditing
public class ApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private final Environment environment;
    @Autowired
    public ApplicationConfig(Environment environment) {
        this.environment = environment;
    }
    @PostConstruct
    public void init() {
        logger.info("🚀 Coding Battle Backend initialized on profile(s): {}", String.join(", ", environment.getActiveProfiles()));
    }
}