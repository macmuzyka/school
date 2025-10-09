package com.school.configuration;

import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MongockConfig {
    @Bean
    public MongockInitializingBeanRunner mongockInitializingBeanRunner(SpringDataMongoV4Driver driver,
                                                                       ApplicationContext context) {
        return MongockSpringboot.builder()
                .setDriver(driver)
                .setSpringContext(context)
                .addMigrationScanPackage("com.school.service.mongodbmigration")
                .buildInitializingBeanRunner();
    }



}