package com.school.configuration;

import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.school.repository.nosql.mongodb")
public class MongoConfig {
    @Bean
    @Primary
    public SpringDataMongoV4Driver mongockConnectionDriver(MongoTemplate mongoTemplate) {
        // Use default lock collection ("mongockLock") for migrations
        return SpringDataMongoV4Driver.withDefaultLock(mongoTemplate);
    }
}