package by.arvisit.cabapp.ridesservice.mock;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;

@Component
public class WireMockService {

    private static final String URL_PASSENGERS_ID_TEMPLATE = "/api/v1/passengers/{0}";
    private static final String URL_DRIVERS_ID_TEMPLATE = "/api/v1/drivers/{0}";
    private static final String URL_DRIVERS_ID_AVAIlABILITY_TEMPLATE = "/api/v1/drivers/{0}/availability";
    private static final String URL_PAYMENTS = "/api/v1/passenger-payments";

    @Autowired
    private ObjectMapper objectMapper;

    public void mockResponseForPassengerClientGetPassengerById(PassengerResponseDto passenger)
            throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(passenger);
        String url = MessageFormat.format(URL_PASSENGERS_ID_TEMPLATE, passenger.id());
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)));
    }

    public void mockResponseForDriverClientGetDriverById(DriverResponseDto driver) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(driver);
        String url = MessageFormat.format(URL_DRIVERS_ID_TEMPLATE, driver.id());
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)));
    }

    public void mockResponseForDriverClientUpdateAvailability(DriverResponseDto driver) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(driver);
        String url = MessageFormat.format(URL_DRIVERS_ID_AVAIlABILITY_TEMPLATE, driver.id());
        WireMock.stubFor(WireMock.patch(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)));
    }

    public void mockResponseForPaymentClientSave(PassengerPaymentResponseDto payment) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(payment);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(URL_PAYMENTS))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)));
    }
}
