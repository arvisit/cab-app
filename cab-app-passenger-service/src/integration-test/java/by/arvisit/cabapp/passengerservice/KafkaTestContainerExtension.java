package by.arvisit.cabapp.passengerservice;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    static KafkaContainer kafkaContainer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));
        kafkaContainer.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        kafkaContainer.stop();
    }

}
