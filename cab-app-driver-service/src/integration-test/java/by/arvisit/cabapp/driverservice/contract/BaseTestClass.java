package by.arvisit.cabapp.driverservice.contract;

import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.CONTRACT_DRIVER_PAYMENT_ID;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.CONTRACT_DRIVER_RIDES_ID;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getDriverForPaymentService;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getDriverForRidesService;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.ActiveProfiles;

import by.arvisit.cabapp.driverservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.driverservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.driverservice.controller.DriverController;
import by.arvisit.cabapp.driverservice.service.DriverService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@AutoConfigureMessageVerifier
public class BaseTestClass {

    private static final boolean NEW_IS_AVAILABLE = false;

    @Autowired
    private DriverController driverController;

    @MockBean
    private DriverService driverService;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(driverController);

        when(driverService.getDriverById(CONTRACT_DRIVER_RIDES_ID))
                .thenReturn(getDriverForRidesService().build());
        when(driverService.updateAvailability(CONTRACT_DRIVER_RIDES_ID, NEW_IS_AVAILABLE))
                .thenReturn(getDriverForRidesService()
                        .withIsAvailable(NEW_IS_AVAILABLE)
                        .build());
        when(driverService.getDriverById(CONTRACT_DRIVER_PAYMENT_ID))
                .thenReturn(getDriverForPaymentService().build());
    }
}
