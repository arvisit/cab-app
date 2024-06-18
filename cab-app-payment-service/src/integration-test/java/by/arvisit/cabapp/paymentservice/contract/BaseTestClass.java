package by.arvisit.cabapp.paymentservice.contract;

import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getPassengerPaymentRequestToSaveFromRides;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getSavedPassengerPaymentForRides;
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

import by.arvisit.cabapp.paymentservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.paymentservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.paymentservice.controller.PassengerPaymentController;
import by.arvisit.cabapp.paymentservice.service.PassengerPaymentService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@AutoConfigureMessageVerifier
public class BaseTestClass {

    @Autowired
    private PassengerPaymentController passengerPaymentController;
    @MockBean
    private PassengerPaymentService passengerPaymentService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(passengerPaymentController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper)).build();
        RestAssuredMockMvc.mockMvc(mockMvc);

        when(passengerPaymentService.save(getPassengerPaymentRequestToSaveFromRides().build()))
                .thenReturn(getSavedPassengerPaymentForRides().build());
    }
}
