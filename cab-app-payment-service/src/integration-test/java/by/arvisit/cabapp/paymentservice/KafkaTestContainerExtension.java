package by.arvisit.cabapp.paymentservice;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    private static final String SPRING_KAFKA_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    static KafkaContainer kafkaContainer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (System.getProperty(SPRING_KAFKA_BOOTSTRAP_SERVERS) == null) {
            kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));
            kafkaContainer.start();
            System.setProperty(SPRING_KAFKA_BOOTSTRAP_SERVERS, kafkaContainer.getBootstrapServers());
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    }

}
