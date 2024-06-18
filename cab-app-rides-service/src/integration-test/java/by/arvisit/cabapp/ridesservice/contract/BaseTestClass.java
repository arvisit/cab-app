package by.arvisit.cabapp.ridesservice.contract;

import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.CONTRACT_RIDE_PAYMENT_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getRideForPayment;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import by.arvisit.cabapp.ridesservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.ridesservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.ridesservice.controller.RideController;
import by.arvisit.cabapp.ridesservice.service.RideService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@AutoConfigureMessageVerifier
public class BaseTestClass {

    @Autowired
    private RideController rideController;
    @MockBean
    private RideService rideService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rideController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();
        RestAssuredMockMvc.mockMvc(mockMvc);

        when(rideService.getRideById(CONTRACT_RIDE_PAYMENT_ID))
                .thenReturn(getRideForPayment().build());
    }
}
