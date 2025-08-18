package com.school.condition.run;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

public class KafkaDockerRunExtension implements BeforeAllCallback {

    public static String containerId;
    public static String kafkaKraftForTests = "kafka-kraft-for-tests";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        containerId = startKafkaContainer();
    }

    private String startKafkaContainer() throws IOException, InterruptedException {
        Process process = new ProcessBuilder(
                "docker", "run", "-d",
                "--name", kafkaKraftForTests,
                "-p", "9092:9092",
                "bashj79/kafka-kraft:latest"
        ).start();

        String id = new String(process.getInputStream().readAllBytes()).trim();
        System.out.println("Started Kafka container: " + id);
        return id;
    }
}
