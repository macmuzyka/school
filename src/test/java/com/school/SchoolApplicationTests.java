package com.school;

import com.school.condition.run.KafkaDockerRunExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("prod")
@ExtendWith(KafkaDockerRunExtension.class)
class SchoolApplicationTests {
    @Test
    void contextLoads() {
    }
}
