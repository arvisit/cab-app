package by.arvisit.cabapp.ridesservice.component;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;

@CucumberContextConfiguration
@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-promo-codes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "classpath:sql/add-rides.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "classpath:sql/delete-rides.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS),
        @Sql(scripts = "classpath:sql/delete-promo-codes.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
public class CucumberComponentTestConfiguration {

    private static final int WIRE_MOCK_SERVER_PORT = 8480;
    private static final String SPRING_KAFKA_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    static KafkaContainer kafkaContainer;

    @LocalServerPort
    private Integer serverPort;
    private WireMockServer wireMockServer;

    @BeforeAll
    public static void setUpDB() {
        System.setProperty("spring.datasource.url", "jdbc:tc:postgresql:15-alpine:///");
    }

    @BeforeAll
    public static void setUpKafka() {
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

    @Before
    public void setUpWireMockServer() {
        wireMockServer = new WireMockServer(WIRE_MOCK_SERVER_PORT);
        wireMockServer.start();
        WireMock.configureFor("localhost", WIRE_MOCK_SERVER_PORT);
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }
}
