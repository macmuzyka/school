package com.school.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.school.repository",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*mongodb.*"))
@EnableMongoRepositories(basePackages = "com.school.repository",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*sql.*"))
public class DataStoreConfig { }
