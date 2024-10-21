package com.school;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan(basePackages = "{com.school.service}")
class SchoolApplicationTests {

    @Test
    void contextLoads() {
    }

}
