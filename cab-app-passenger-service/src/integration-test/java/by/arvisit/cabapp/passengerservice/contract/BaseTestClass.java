package by.arvisit.cabapp.passengerservice.contract;

import static by.arvisit.cabapp.passengerservice.util.PassengerIntegrationTestData.CONTRACT_PASSENGER_3_RIDES_ID;
import static by.arvisit.cabapp.passengerservice.util.PassengerIntegrationTestData.CONTRACT_PASSENGER_5_RIDES_ID;
import static by.arvisit.cabapp.passengerservice.util.PassengerIntegrationTestData.CONTRACT_PASSENGER_PAYMENT_ID;
import static by.arvisit.cabapp.passengerservice.util.PassengerIntegrationTestData.getPassenger3ForRidesService;
import static by.arvisit.cabapp.passengerservice.util.PassengerIntegrationTestData.getPassenger5ForRidesService;
import static by.arvisit.cabapp.passengerservice.util.PassengerIntegrationTestData.getPassengerForPaymentService;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.ActiveProfiles;

import by.arvisit.cabapp.passengerservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.passengerservice.controller.PassengerController;
import by.arvisit.cabapp.passengerservice.service.PassengerService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@AutoConfigureMessageVerifier
public class BaseTestClass {

    @Autowired
    private PassengerController passengerController;

    @MockBean
    private PassengerService passengerService;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(passengerController);

        when(passengerService.getPassengerById(CONTRACT_PASSENGER_3_RIDES_ID))
                .thenReturn(getPassenger3ForRidesService().build());
        when(passengerService.getPassengerById(CONTRACT_PASSENGER_5_RIDES_ID))
                .thenReturn(getPassenger5ForRidesService().build());
        when(passengerService.getPassengerById(CONTRACT_PASSENGER_PAYMENT_ID))
                .thenReturn(getPassengerForPaymentService().build());
    }
}
