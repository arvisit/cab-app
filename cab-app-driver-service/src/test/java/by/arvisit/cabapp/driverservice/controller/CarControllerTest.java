package by.arvisit.cabapp.driverservice.controller;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_CAR_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_CARS;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_CARS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.service.CarService;
import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = CarController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class CarControllerTest {

    private static final String MALFORMED_UUID_MESSAGE = "must be a valid UUID";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CarService carService;

    @Nested
    class GetCars {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<CarResponseDto> responseDto = getCarResponseDtoInListContainer()
                    .build();

            when(carService.getCars(any(Pageable.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_CARS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidCars_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<CarResponseDto> responseDto = getCarResponseDtoInListContainer()
                    .build();

            when(carService.getCars(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_CARS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @Test
        void shouldMapToBusinessModel_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<CarResponseDto> responseDto = getCarResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(carService.getCars(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_CARS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(carService, times(1)).getCars(pageable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<CarResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<CarResponseDto>>() {
                    });

            assertThat(result)
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class GetCarById {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(get(URL_CARS_ID_TEMPLATE, DEFAULT_CAR_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.controller.CarControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_CARS_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(carService.getCarById(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_CARS_ID_TEMPLATE, DEFAULT_CAR_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidCar_whenExistingId() throws Exception {
            String existingId = DEFAULT_CAR_ID_STRING;
            CarResponseDto responseDto = getCarResponseDto().build();

            when(carService.getCarById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_CARS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            String existingId = DEFAULT_CAR_ID_STRING;
            CarResponseDto responseDto = getCarResponseDto().build();

            when(carService.getCarById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_CARS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(carService, times(1)).getCarById(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            CarResponseDto result = objectMapper.readValue(actualResponseBody, CarResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

    }

    static Stream<String> malformedUUIDs() {
        return Stream.of("3abcc6a1-94da-4185-1-8a11c1b8efd2", "3abcc6a1-94da-4185-aaa1-8a11c1b8efdw",
                "3ABCC6A1-94DA-4185-AAA1-8A11C1B8EFD2", "   ", "1234", "abc", "111122223333444a",
                "111122223333-444");
    }

}
