package by.arvisit.cabapp.driverservice.controller;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.COLOR_ID_WHITE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_CAR_MANUFACTURER_ID;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_DRIVER_EMAIL;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_DRIVER_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.IS_AVAILABLE_KEY;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.IS_AVAILABLE_REQUEST_PARAM;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.NAME_REQUEST_PARAM;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.NOT_ALLOWED_REQUEST_PARAM;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_AVAILABLE_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_DRIVERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_DRIVERS_ID_AVAILABILITY_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_DRIVERS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_DRIVERS_PARAM_VALUE_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverRequestDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
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
import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.service.CarManufacturerService;
import by.arvisit.cabapp.driverservice.service.ColorService;
import by.arvisit.cabapp.driverservice.service.DriverService;
import by.arvisit.cabapp.driverservice.util.DriverTestData;
import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = DriverController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class DriverControllerTest {

    private static final String SIZE_NAME_MESSAGE = "Name should be no longer than 100";
    private static final String SIZE_EMAIL_MESSAGE = "Email should be no longer than 100";
    private static final String NOT_BLANK_NAME_MESSAGE = "Name should not be blank";
    private static final String NOT_BLANK_EMAIL_MESSAGE = "Email should not be blank";
    private static final String NOT_NULL_CARD_NUMBER_MESSAGE = "Card number should not be null";
    private static final String NOT_NULL_CAR_MESSAGE = "Car data should not be null";
    private static final String NOT_NULL_CAR_COLOR_MESSAGE = "Car color id should not be null";
    private static final String NO_SUCH_CAR_COLOR_MESSAGE = "There is no color with id: " + COLOR_ID_WHITE;
    private static final String NOT_NULL_CAR_MANUFACTURER_MESSAGE = "Car manufacturer id should not be null";
    private static final String NO_SUCH_CAR_MANUFACTURER_MESSAGE = "There is no car manufacturer with id: "
            + DEFAULT_CAR_MANUFACTURER_ID;
    private static final String NOT_NULL_CAR_REGISTRATION_NUMBER_MESSAGE = "Car registration number should not be null";
    private static final String PATTERN_CAR_REGISTRATION_NUMBER_MESSAGE = "Cars with ICE should have registration number according to pattern - 0000AA-1 (4 digits, 2 chars from (A, B, E, I, K, M, H, O, P, C, T, X), dash, 1 digit from 1 to 7). For electric cars - E000AA-1 (char E, 3 digits, 2 chars from (A, B, E, I, K, M, H, O, P, C, T, X), dash, 1 digit from 1 to 7)";
    private static final String MALFORMED_EMAIL_MESSAGE = "Email should be well-formed";
    private static final String MALFORMED_UUID_MESSAGE = "must be a valid UUID";
    private static final String PATTERN_CARD_NUMBER_MESSAGE = "Card number should consist of 16 digits";
    private static final String UNPARSEABLE_BOOLEAN_MESSAGE = "Unparseable boolean values detected";
    private static final String PATCH_WITH_NO_IS_AVAILABLE_KEY_MESSAGE = "Patch should contain key: isAvailable";
    private static final String UNREADABLE_REQUEST_BODY_MESSAGE = "Request body is missing or could not be read";
    private static final String MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE = "Patch should contain one key:value pair";
    private static final String PATCH_NULL_VALUE_FOR_IS_AVAILABLE_KEY_MESSAGE = "isAvailable value should not be null";
    private static final String LETTERS_101 = "TVQDfZXYIzFNzhzskpZiwULzGOikAuVtwRPnYaCjQjdeMJKcEbvpuiQNUmaERmPtyDwFHPHaiHnWZHmoXrRNUVDWVOkZFhlVOBuMC";
    private static final String EMPTY_STRING = "";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DriverService driverService;
    @MockBean
    private CarManufacturerService carManufacturerService;
    @MockBean
    private ColorService colorService;

    @Nested
    class SaveDriver {

        @Test
        void shouldReturn201_whenValidInput() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.save(any(DriverRequestDto.class)))
                    .thenReturn(responseDto);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            MvcResult mvcResult = mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            verify(driverService, times(1)).save(requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverResponseDto result = objectMapper.readValue(actualResponseBody, DriverResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidDriver_whenValidInput() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.save(any(DriverRequestDto.class)))
                    .thenReturn(responseDto);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            MvcResult mvcResult = mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#blankStrings")
        void shouldReturn400_whenBlankName(String name) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withName(name)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_BLANK_NAME_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNameLongerThan100() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withName(LETTERS_101)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = SIZE_NAME_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#blankStrings")
        void shouldReturn400_whenBlankEmail(String email) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withEmail(email)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_BLANK_EMAIL_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedEmails")
        void shouldReturn400_whenMalformedEmail(String email) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withEmail(email)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = MALFORMED_EMAIL_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenEmailLongerThan100() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withEmail(LETTERS_101)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = SIZE_EMAIL_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedCardNumbers")
        void shouldReturn400_whenMalformedCardNumber(String cardNumber) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCardNumber(cardNumber)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = PATTERN_CARD_NUMBER_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCardNumber() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCardNumber(null)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CARD_NUMBER_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCar() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(null)
                    .build();

            String expectedContent = NOT_NULL_CAR_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCarColor() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withColorId(null)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CAR_COLOR_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNoSuchCarColor() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withColorId(COLOR_ID_WHITE)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(false);

            String expectedContent = NO_SUCH_CAR_COLOR_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCarManufacturer() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withManufacturerId(null)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CAR_MANUFACTURER_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNoSuchCarManufacturer() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withManufacturerId(DEFAULT_CAR_MANUFACTURER_ID)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(false);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NO_SUCH_CAR_MANUFACTURER_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCarRegistrationNumber() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withRegistrationNumber(null)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CAR_REGISTRATION_NUMBER_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedCarRegistrationNumbers")
        void shouldReturn400_whenMalformedCarRegistrationNumber(String registrationNumber) throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withRegistrationNumber(registrationNumber)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = PATTERN_CAR_REGISTRATION_NUMBER_MESSAGE;

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn409_whenEmailInUse() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();

            when(driverService.save(any(DriverRequestDto.class)))
                    .thenThrow(UsernameAlreadyExistsException.class);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            mockMvc.perform(post(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class UpdateDriver {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();

            when(driverService.update(anyString(), any(DriverRequestDto.class)))
                    .thenThrow(EntityNotFoundException.class);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.update(anyString(), any(DriverRequestDto.class)))
                    .thenReturn(responseDto);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String existingId = DEFAULT_DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverService, times(1)).update(existingId, requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverResponseDto result = objectMapper.readValue(actualResponseBody, DriverResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidDriver_whenValidInput() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.update(anyString(), any(DriverRequestDto.class)))
                    .thenReturn(responseDto);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String existingId = DEFAULT_DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#blankStrings")
        void shouldReturn400_whenBlankName(String name) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withName(name)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_BLANK_NAME_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNameLongerThan100() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withName(LETTERS_101)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = SIZE_NAME_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#blankStrings")
        void shouldReturn400_whenBlankEmail(String email) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withEmail(email)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_BLANK_EMAIL_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedEmails")
        void shouldReturn400_whenMalformedEmail(String email) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withEmail(email)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = MALFORMED_EMAIL_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenEmailLongerThan100() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withEmail(LETTERS_101)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = SIZE_EMAIL_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedCardNumbers")
        void shouldReturn400_whenMalformedCardNumber(String cardNumber) throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCardNumber(cardNumber)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = PATTERN_CARD_NUMBER_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCardNumber() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCardNumber(null)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CARD_NUMBER_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCar() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(null)
                    .build();

            String expectedContent = NOT_NULL_CAR_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCarColor() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withColorId(null)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CAR_COLOR_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNoSuchCarColor() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withColorId(COLOR_ID_WHITE)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(false);

            String expectedContent = NO_SUCH_CAR_COLOR_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCarManufacturer() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withManufacturerId(null)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CAR_MANUFACTURER_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNoSuchCarManufacturer() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withManufacturerId(DEFAULT_CAR_MANUFACTURER_ID)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(false);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NO_SUCH_CAR_MANUFACTURER_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullCarRegistrationNumber() throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withRegistrationNumber(null)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = NOT_NULL_CAR_REGISTRATION_NUMBER_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedCarRegistrationNumbers")
        void shouldReturn400_whenMalformedCarRegistrationNumber(String registrationNumber) throws Exception {
            CarRequestDto carRequestDto = DriverTestData.getCarRequestDto()
                    .withRegistrationNumber(registrationNumber)
                    .build();
            DriverRequestDto requestDto = getDriverRequestDto()
                    .withCar(carRequestDto)
                    .build();

            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            String expectedContent = PATTERN_CAR_REGISTRATION_NUMBER_MESSAGE;

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn409_whenEmailInUse() throws Exception {
            DriverRequestDto requestDto = getDriverRequestDto().build();

            when(driverService.update(anyString(), any(DriverRequestDto.class)))
                    .thenThrow(UsernameAlreadyExistsException.class);
            when(carManufacturerService.existsById(Mockito.anyInt()))
                    .thenReturn(true);
            when(colorService.existsById(Mockito.anyInt()))
                    .thenReturn(true);

            mockMvc.perform(put(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class DeleteDriver {

        @Test
        void shouldReturn204_whenValidInput() throws Exception {
            mockMvc.perform(delete(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            doThrow(EntityNotFoundException.class).when(driverService)
                    .delete(anyString());

            mockMvc.perform(delete(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(delete(URL_DRIVERS_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            doNothing().when(driverService)
                    .delete(anyString());

            String existingId = DEFAULT_DRIVER_ID_STRING;
            mockMvc.perform(delete(URL_DRIVERS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(driverService, times(1)).delete(existingId);
        }
    }

    @Nested
    class GetDriverById {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(get(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_DRIVERS_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(driverService.getDriverById(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_DRIVERS_ID_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidDriver_whenExistingId() throws Exception {
            String existingId = DEFAULT_DRIVER_ID_STRING;
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.getDriverById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVERS_ID_TEMPLATE, existingId)
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
            String existingId = DEFAULT_DRIVER_ID_STRING;
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.getDriverById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVERS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverService, times(1)).getDriverById(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverResponseDto result = objectMapper.readValue(actualResponseBody, DriverResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

    }

    @Nested
    class GetDriverByEmail {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(get(URL_DRIVERS_EMAIL_TEMPLATE, DEFAULT_DRIVER_EMAIL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedEmails")
        void shouldReturn400_whenMalformedEmail(String email) throws Exception {
            mockMvc.perform(get(URL_DRIVERS_EMAIL_TEMPLATE, email)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn404_whenNonExistingEmail() throws Exception {
            when(driverService.getDriverByEmail(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_DRIVERS_EMAIL_TEMPLATE, DEFAULT_DRIVER_EMAIL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidDriver_whenExistingEmail() throws Exception {
            String existingEmail = DEFAULT_DRIVER_EMAIL;
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.getDriverByEmail(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVERS_EMAIL_TEMPLATE, existingEmail)
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
        void shouldMapToBusinessModel_whenExistingEmail() throws Exception {
            String existingEmail = DEFAULT_DRIVER_EMAIL;
            DriverResponseDto responseDto = getDriverResponseDto().build();

            when(driverService.getDriverByEmail(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVERS_EMAIL_TEMPLATE, existingEmail)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverService, times(1)).getDriverByEmail(existingEmail);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverResponseDto result = objectMapper.readValue(actualResponseBody, DriverResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

    }

    @Nested
    class GetDrivers {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<DriverResponseDto> responseDto = getDriverResponseDtoInListContainer()
                    .build();

            when(driverService.getDrivers(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidDrivers_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<DriverResponseDto> responseDto = getDriverResponseDtoInListContainer()
                    .build();

            when(driverService.getDrivers(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVERS)
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
            ListContainerResponseDto<DriverResponseDto> responseDto = getDriverResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(driverService.getDrivers(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverService, times(1)).getDrivers(pageable, Collections.emptyMap());

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<DriverResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<DriverResponseDto>>() {
                    });

            assertThat(result)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturn400_whenNotAllowedRequestParam() throws Exception {
            mockMvc.perform(get(URL_DRIVERS_PARAM_VALUE_TEMPLATE, NOT_ALLOWED_REQUEST_PARAM, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400_whenBlankRequestParamValue() throws Exception {
            mockMvc.perform(get(URL_DRIVERS_PARAM_VALUE_TEMPLATE, NAME_REQUEST_PARAM, EMPTY_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedBooleans")
        void shouldReturn400_whenNotParseableBooleanForIsAvailableRequestParam(String value) throws Exception {
            String expectedContent = UNPARSEABLE_BOOLEAN_MESSAGE;

            mockMvc.perform(get(URL_DRIVERS_PARAM_VALUE_TEMPLATE, IS_AVAILABLE_REQUEST_PARAM, value)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class GetAvailableDrivers {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<DriverResponseDto> responseDto = getDriverResponseDtoInListContainer()
                    .build();

            when(driverService.getAvailableDrivers(any(Pageable.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_AVAILABLE_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidDrivers_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<DriverResponseDto> responseDto = getDriverResponseDtoInListContainer()
                    .build();

            when(driverService.getAvailableDrivers(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_AVAILABLE_DRIVERS)
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
            ListContainerResponseDto<DriverResponseDto> responseDto = getDriverResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(driverService.getAvailableDrivers(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_AVAILABLE_DRIVERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverService, times(1)).getAvailableDrivers(pageable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<DriverResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<DriverResponseDto>>() {
                    });

            assertThat(result)
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class UpdateAvailability {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            Map<String, Boolean> requestDto = Map.of(IS_AVAILABLE_KEY, true);

            mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.driverservice.util.DriverTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            Map<String, Boolean> requestDto = Map.of(IS_AVAILABLE_KEY, true);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            Map<String, Boolean> requestDto = Map.of(IS_AVAILABLE_KEY, true);

            when(driverService.updateAvailability(anyString(), anyBoolean()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            boolean newIsAvailable = true;
            Map<String, Boolean> requestDto = Map.of(IS_AVAILABLE_KEY, newIsAvailable);
            DriverResponseDto responseDto = getDriverResponseDto()
                    .withIsAvailable(newIsAvailable)
                    .build();

            when(driverService.updateAvailability(anyString(), anyBoolean()))
                    .thenReturn(responseDto);

            String existingId = DEFAULT_DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverService, times(1)).updateAvailability(existingId, newIsAvailable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverResponseDto result = objectMapper.readValue(actualResponseBody, DriverResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidDriver_whenValidInput() throws Exception {
            boolean newIsAvailable = true;
            Map<String, Boolean> requestDto = Map.of(IS_AVAILABLE_KEY, newIsAvailable);
            DriverResponseDto responseDto = getDriverResponseDto()
                    .withIsAvailable(newIsAvailable)
                    .build();

            when(driverService.updateAvailability(anyString(), anyBoolean()))
                    .thenReturn(responseDto);

            String existingId = DEFAULT_DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @Test
        void shouldReturn400_whenNullBody() throws Exception {
            Map<String, Boolean> requestDto = null;

            String expectedContent = UNREADABLE_REQUEST_BODY_MESSAGE;

            mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenMoreThanOneKeyValuePairMapInBody() throws Exception {
            boolean newIsAvailable = true;
            Map<String, Boolean> requestDto = Map.of(IS_AVAILABLE_KEY, newIsAvailable, NAME_REQUEST_PARAM,
                    newIsAvailable);

            String expectedContent = MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE;

            mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBodyContainsNoIsAvailableKey() throws Exception {
            Map<String, Boolean> requestDto = Map.of(NAME_REQUEST_PARAM, true);

            String expectedContent = PATCH_WITH_NO_IS_AVAILABLE_KEY_MESSAGE;

            mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBooleanValueInBodyIsNull() throws Exception {
            Map<String, Boolean> requestDto = Collections.singletonMap(IS_AVAILABLE_KEY, null);

            String expectedContent = PATCH_NULL_VALUE_FOR_IS_AVAILABLE_KEY_MESSAGE;

            mockMvc.perform(patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }
}
