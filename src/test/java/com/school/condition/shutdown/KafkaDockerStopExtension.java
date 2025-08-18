package com.school.condition.shutdown;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

import static com.school.condition.run.KafkaDockerRunExtension.containerId;
import static com.school.condition.run.KafkaDockerRunExtension.kafkaKraftForTests;

public class KafkaDockerStopExtension implements AfterAllCallback {
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (containerId != null) {
            System.out.println("Attempting to shut down & remove container: " + containerId);
            stopKafkaContainer();
        }
    }

    private void stopKafkaContainer() throws IOException, InterruptedException {
        Process stopProcess = new ProcessBuilder("docker", "stop", kafkaKraftForTests).start();
        int stopExitCode = stopProcess.waitFor();
        if (stopExitCode == 0) {
            System.out.println("Stopped Kafka container: " + kafkaKraftForTests);
            Process containerRemovingProcess = new ProcessBuilder("docker", "rm", kafkaKraftForTests).start();
            int removingContainerExitCode = containerRemovingProcess.waitFor();
            if (removingContainerExitCode == 0) {
                System.out.println("Removed Kafka container: " + kafkaKraftForTests);
            }
        }
    }
}
