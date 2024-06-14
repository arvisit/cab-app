package by.arvisit.cabapp.paymentservice.mock;

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
import by.arvisit.cabapp.common.dto.rides.RideResponseDto;

@Component
public class WireMockService {

    private static final String URL_PASSENGERS_ID_TEMPLATE = "/api/v1/passengers/{0}";
    private static final String URL_DRIVERS_ID_TEMPLATE = "/api/v1/drivers/{0}";
    private static final String URL_RIDES_ID_TEMPLATE = "/api/v1/rides/{0}";

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

    public void mockResponseForRideClientGetRideById(RideResponseDto ride) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(ride);
        String url = MessageFormat.format(URL_RIDES_ID_TEMPLATE, ride.id());
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(body)));
    }
}
