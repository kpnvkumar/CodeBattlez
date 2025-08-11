// File: src/main/java/com/codingbattle/config/MongoConfig.java
package com.example.codingbattle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "coding_battle_db";
    }

    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        List<Object> converters = new ArrayList<>();

        // LocalDateTime to Date converter
        converters.add(new org.springframework.core.convert.converter.Converter<LocalDateTime, Date>() {
            @Override
            public Date convert(LocalDateTime source) {
                return Date.from(source.toInstant(ZoneOffset.UTC));
            }
        });

        // Date to LocalDateTime converter
        converters.add(new org.springframework.core.convert.converter.Converter<Date, LocalDateTime>() {
            @Override
            public LocalDateTime convert(Date source) {
                return LocalDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
            }
        });

        return new MongoCustomConversions(converters);
    }
}