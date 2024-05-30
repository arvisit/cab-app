package by.arvisit.cabapp.passengerservice.component;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;

@CucumberContextConfiguration
@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-passengers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-passengers.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class CucumberComponentTestConfiguration {

    private static final String SPRING_KAFKA_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    static KafkaContainer kafkaContainer;

    @LocalServerPort
    private int serverPort;

    @BeforeAll
    public static void setUpDB() {
        System.setProperty("spring.datasource.url", "jdbc:tc:postgresql:15-alpine:///");
    }

    @BeforeAll
    public static void setUpKafka() {
        System.out.println("BOOTSTRAP KAFKA " + System.getProperty(SPRING_KAFKA_BOOTSTRAP_SERVERS));
        if (System.getProperty(SPRING_KAFKA_BOOTSTRAP_SERVERS) == null) {
            kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));
            kafkaContainer.start();
            System.setProperty(SPRING_KAFKA_BOOTSTRAP_SERVERS, kafkaContainer.getBootstrapServers());
        }
    }

    @Before
    public void setUpPort() {
        RestAssured.port = serverPort;
    }
}
