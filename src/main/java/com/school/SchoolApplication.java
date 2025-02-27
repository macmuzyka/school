package com.school;

import com.school.configuration.ApplicationConfig;
import com.school.configuration.DatabaseBackupConfig;
import com.school.configuration.FileConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({FileConfig.class, ApplicationConfig.class, DatabaseBackupConfig.class})
@EnableJpaAuditing
@EnableScheduling
public class SchoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
    }
}
