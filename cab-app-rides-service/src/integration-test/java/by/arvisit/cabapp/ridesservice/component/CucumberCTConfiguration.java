package by.arvisit.cabapp.ridesservice.component;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

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
@WireMockTest(httpPort = 8480)
public class CucumberCTConfiguration {

    static KafkaContainer kafkaContainer;

    @LocalServerPort
    private Integer serverPort;

    @BeforeAll
    public static void setUpDB() {
        System.setProperty("spring.datasource.url", "jdbc:tc:postgresql:15-alpine:///");
    }

    @BeforeAll
    public static void setUpKafka() {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));
        kafkaContainer.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
    }

    @Before
    public void setUpPort() {
        RestAssured.port = serverPort;
    }

}
