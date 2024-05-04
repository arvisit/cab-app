package by.arvisit.cabapp.ridesservice.controller;

import static by.arvisit.cabapp.common.util.CommonConstants.EUROPE_MINSK_TIMEZONE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.BOOK_RIDE_REQUEST_PARAM;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.DRIVER_ID_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.DRIVER_ID_REQUEST_PARAM;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.DRIVER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.NOT_ALLOWED_REQUEST_PARAM;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PASSENGER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PAYMENT_METHOD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_DEFAULT_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_KEYWORD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_DRIVER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_DRIVER_SCORE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PASSENGER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PASSENGER_SCORE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PAYMENT_METHOD_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_DRIVER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_DRIVER_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_ACCEPT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_APPLY_PROMO_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_BEGIN_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_CANCEL_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_END_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_FINISH_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_SCORE_DRIVER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_PARAM_VALUE_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_PASSENGER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_RIDES_PASSENGER_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getAcceptedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBeganRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBookedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBookedWithPromoCodeRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getCanceledRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getDriverRating;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getEndedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPassengerRating;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getRideRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getRideResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.service.RatingService;
import by.arvisit.cabapp.ridesservice.service.RideService;
import by.arvisit.cabapp.ridesservice.util.AppConstants;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = RideController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class RideControllerTest {

    private static final int START_ADDRESS_MAX_SIZE = 100;
    private static final int DESTINATION_ADDRESS_MAX_SIZE = 100;
    private static final String SIZE_DESTINATION_ADDRESS_MESSAGE = "Destination address should not be longer than "
            + DESTINATION_ADDRESS_MAX_SIZE;
    private static final String SIZE_START_ADDRESS_MESSAGE = "Start address should not be longer than "
            + START_ADDRESS_MAX_SIZE;
    private static final String NOT_BLANK_START_ADDRESS_MESSAGE = "Start address should not be blank";
    private static final String NOT_BLANK_DESTINATION_ADDRESS_MESSAGE = "Destination address should not be blank";
    private static final String NOT_BLANK_PAYMENT_METHOD_MESSAGE = "Payment method should not be blank";
    private static final String INVALID_PAYMENT_METHOD_MESSAGE = "Payment method should be BANK_CARD or CASH";
    private static final String MALFORMED_UUID_MESSAGE = "must be a valid UUID";
    private static final String UNPARSEABLE_DATE_MESSAGE = "Unparseable date values detected";
    private static final String UNPARSEABLE_UUID_MESSAGE = "Unparseable UUID values detected";
    private static final String PATCH_SHOULD_CONTAIN_KEY_MESSAGE_TEMPLATE_PREFIX = "Patch should contain key: ";
    private static final String UNREADABLE_REQUEST_BODY_MESSAGE = "Request body is missing or could not be read";
    private static final String MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE = "Patch should contain one key:value pair";
    private static final String PATCH_DRIVER_ID_BlANK_MESSAGE = "Driver id should not be blank";
    private static final String PATCH_PROMO_CODE_KEYWORD_BlANK_MESSAGE = "Promo code should not be blank";
    private static final String PATCH_PAYMENT_METHOD_BlANK_MESSAGE = "Payment method should not be blank";
    private static final String PATCH_SCORE_NULL_MESSAGE = "Score value should not be null";
    private static final String PATCH_SCORE_GREATER_THAN_MAX_MESSAGE = "Score value should not be greater than "
            + AppConstants.MAX_SCORE;
    private static final String PATCH_SCORE_NEGATIVE_MESSAGE = "Score should have positive or zero value";
    private static final String LETTERS_101 = "TVQDfZXYIzFNzhzskpZiwULzGOikAuVtwRPnYaCjQjdeMJKcEbvpuiQNUmaERmPtyDwFHPHaiHnWZHmoXrRNUVDWVOkZFhlVOBuMC";
    private static final String EMPTY_STRING = "";
    private static final String[] TIMESTAMP_FIELDS = { "bookRide", "cancelRide", "acceptRide", "beginRide", "endRide",
            "finishRide" };
    private static final String[] IN_LIST_CONTAINER_TIMESTAMP_FIELDS = { "values.bookRide", "values.cancelRide",
            "values.acceptRide", "values.beginRide", "values.endRide", "values.finishRide" };
    private static final int NEGATIVE_SCORE = -1;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RideService rideService;
    @MockBean
    private RatingService ratingService;

    @Nested
    class SaveRide {

        @Test
        void shouldReturn201_whenValidInput() throws Exception {
            RideRequestDto requestDto = getRideRequestDto().build();

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            RideRequestDto requestDto = getRideRequestDto().build();
            RideResponseDto responseDto = getBookedRideResponseDto().build();

            when(rideService.save(any(RideRequestDto.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            verify(rideService, times(1)).save(requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);

            assertThat(result.bookRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE))
                    .isEqualTo(responseDto.bookRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE));
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            RideRequestDto requestDto = getRideRequestDto().build();
            RideResponseDto responseDto = getBookedRideResponseDto().build();

            when(rideService.save(any(RideRequestDto.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(URL_RIDES)
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
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedPassengerId(String id) throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withPassengerId(id)
                    .build();

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingPassengerId() throws Exception {
            RideRequestDto requestDto = getRideRequestDto().build();

            when(rideService.save(any(RideRequestDto.class)))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#blankStrings")
        void shouldReturn400_whenBlankStartAddress(String startAddress) throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withStartAddress(startAddress)
                    .build();

            String expectedContent = NOT_BLANK_START_ADDRESS_MESSAGE;

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenStartAddressLongerThanMaxAllowed() throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withStartAddress(LETTERS_101)
                    .build();

            String expectedContent = SIZE_START_ADDRESS_MESSAGE;

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#blankStrings")
        void shouldReturn400_whenBlankDestinationAddress(String destinationAddress) throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withDestinationAddress(destinationAddress)
                    .build();

            String expectedContent = NOT_BLANK_DESTINATION_ADDRESS_MESSAGE;

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenDestinationAddressLongerThanMaxAllowed() throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withDestinationAddress(LETTERS_101)
                    .build();

            String expectedContent = SIZE_DESTINATION_ADDRESS_MESSAGE;

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#blankStrings")
        void shouldReturn400_whenBlankPaymentMethod(String paymentMethod) throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withPaymentMethod(paymentMethod)
                    .build();

            String expectedContent = NOT_BLANK_PAYMENT_METHOD_MESSAGE;

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenInvalidPaymentMethod() throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withPaymentMethod(LETTERS_101)
                    .build();

            String expectedContent = INVALID_PAYMENT_METHOD_MESSAGE;

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @EnumSource(PaymentMethodEnum.class)
        void shouldReturn201_whenValidPaymentMethod(PaymentMethodEnum paymentMethod) throws Exception {
            RideRequestDto requestDto = getRideRequestDto()
                    .withPaymentMethod(paymentMethod.toString())
                    .build();

            mockMvc.perform(post(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    class CancelRide {

        @Test
        void shouldReturn200_whenVaildInput() throws Exception {
            mockMvc.perform(patch(URL_RIDES_ID_CANCEL_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            RideResponseDto responseDto = getCanceledRideResponseDto().build();

            when(rideService.cancelRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_CANCEL_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).cancelRide(RIDE_DEFAULT_ID_STRING);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);

            assertThat(result.cancelRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE))
                    .isEqualTo(responseDto.cancelRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE));
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            RideResponseDto responseDto = getCanceledRideResponseDto().build();

            when(rideService.cancelRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_CANCEL_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_CANCEL_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideId() throws Exception {
            when(rideService.cancelRide(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_CANCEL_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturn409_whenUnverifiedRide() throws Exception {
            when(rideService.cancelRide(anyString()))
                    .thenThrow(IllegalStateException.class);

            mockMvc.perform(patch(URL_RIDES_ID_CANCEL_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class AcceptRide {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING);

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedDriverId(String id) throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, id);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideIdOrDriverId() throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING);

            when(rideService.acceptRide(anyString(), anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            String existingDriverId = RIDE_DEFAULT_ID_STRING;
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, existingDriverId);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.acceptRide(anyString(), anyString()))
                    .thenReturn(responseDto);

            String existingRideId = RIDE_DEFAULT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, existingRideId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).acceptRide(existingRideId, existingDriverId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            String existingDriverId = RIDE_DEFAULT_ID_STRING;
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, existingDriverId);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.acceptRide(anyString(), anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
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
            Map<String, String> requestDto = null;

            String expectedContent = UNREADABLE_REQUEST_BODY_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenMoreThanOneKeyValuePairMapInBody() throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING,
                    PROMO_CODE_KEYWORD_KEY, PROMO_CODE_DEFAULT_KEYWORD);

            String expectedContent = MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBodyContainsNoDriverIdKey() throws Exception {
            Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, PROMO_CODE_DEFAULT_KEYWORD);

            String expectedContent = PATCH_SHOULD_CONTAIN_KEY_MESSAGE_TEMPLATE_PREFIX + DRIVER_ID_KEY;

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#blankStrings")
        void shouldReturn400_whenDriverIdValueIsBlank(String value) throws Exception {
            Map<String, String> requestDto = Collections.singletonMap(DRIVER_ID_KEY, value);

            String expectedContent = PATCH_DRIVER_ID_BlANK_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_ACCEPT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class BeginRide {

        @Test
        void shouldReturn200_whenVaildInput() throws Exception {
            mockMvc.perform(patch(URL_RIDES_ID_BEGIN_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            RideResponseDto responseDto = getBeganRideResponseDto().build();

            when(rideService.beginRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_BEGIN_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).beginRide(RIDE_DEFAULT_ID_STRING);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);

            assertThat(result.beginRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE))
                    .isEqualTo(responseDto.beginRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE));
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            RideResponseDto responseDto = getBeganRideResponseDto().build();

            when(rideService.beginRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_BEGIN_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_BEGIN_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideId() throws Exception {
            when(rideService.beginRide(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_BEGIN_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturn409_whenUnverifiedRide() throws Exception {
            when(rideService.beginRide(anyString()))
                    .thenThrow(IllegalStateException.class);

            mockMvc.perform(patch(URL_RIDES_ID_BEGIN_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class EndRide {

        @Test
        void shouldReturn200_whenVaildInput() throws Exception {
            mockMvc.perform(patch(URL_RIDES_ID_END_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            RideResponseDto responseDto = getEndedRideResponseDto().build();

            when(rideService.endRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_END_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).endRide(RIDE_DEFAULT_ID_STRING);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);

            assertThat(result.endRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE))
                    .isEqualTo(responseDto.endRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE));
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            RideResponseDto responseDto = getEndedRideResponseDto().build();

            when(rideService.endRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_END_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_END_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideIdOrPassengerId() throws Exception {
            when(rideService.endRide(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_END_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturn409_whenUnverifiedRide() throws Exception {
            when(rideService.endRide(anyString()))
                    .thenThrow(IllegalStateException.class);

            mockMvc.perform(patch(URL_RIDES_ID_END_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class FinishRide {

        @Test
        void shouldReturn200_whenVaildInput() throws Exception {
            mockMvc.perform(patch(URL_RIDES_ID_FINISH_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            RideResponseDto responseDto = getFinishedRideResponseDto().build();

            when(rideService.finishRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_FINISH_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).finishRide(RIDE_DEFAULT_ID_STRING);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);

            assertThat(result.finishRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE))
                    .isEqualTo(responseDto.finishRide().withZoneSameInstant(EUROPE_MINSK_TIMEZONE));
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            RideResponseDto responseDto = getFinishedRideResponseDto().build();

            when(rideService.finishRide(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_FINISH_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_FINISH_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideId() throws Exception {
            when(rideService.finishRide(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_FINISH_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturn409_whenUnverifiedRide() throws Exception {
            when(rideService.finishRide(anyString()))
                    .thenThrow(IllegalStateException.class);

            mockMvc.perform(patch(URL_RIDES_ID_FINISH_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class ConfirmPayment {

        @Test
        void shouldReturn200_whenVaildInput() throws Exception {
            mockMvc.perform(patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            RideResponseDto responseDto = getFinishedRideResponseDto().build();

            when(rideService.confirmPayment(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).confirmPayment(RIDE_DEFAULT_ID_STRING);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            RideResponseDto responseDto = getFinishedRideResponseDto().build();

            when(rideService.confirmPayment(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideId() throws Exception {
            when(rideService.confirmPayment(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturn409_whenUnverifiedRide() throws Exception {
            when(rideService.confirmPayment(anyString()))
                    .thenThrow(IllegalStateException.class);

            mockMvc.perform(patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class ApplyPromoCode {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, PROMO_CODE_DEFAULT_KEYWORD);

            mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, PROMO_CODE_DEFAULT_KEYWORD);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideIdOrPromoCode() throws Exception {
            Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, PROMO_CODE_DEFAULT_KEYWORD);

            when(rideService.applyPromoCode(anyString(), anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            String existingActivePromoCodeKeyword = PROMO_CODE_DEFAULT_KEYWORD;
            Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, existingActivePromoCodeKeyword);
            RideResponseDto responseDto = getBookedWithPromoCodeRideResponseDto().build();

            when(rideService.applyPromoCode(anyString(), anyString()))
                    .thenReturn(responseDto);

            String existingRideId = RIDE_DEFAULT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, existingRideId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).applyPromoCode(existingRideId, existingActivePromoCodeKeyword);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
            assertThat(result.promoCode())
                    .isEqualTo(existingActivePromoCodeKeyword);
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            String existingActivePromoCodeKeyword = PROMO_CODE_DEFAULT_KEYWORD;
            Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, existingActivePromoCodeKeyword);
            RideResponseDto responseDto = getBookedWithPromoCodeRideResponseDto().build();

            when(rideService.applyPromoCode(anyString(), anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, RIDE_DEFAULT_ID_STRING)
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
            Map<String, String> requestDto = null;

            String expectedContent = UNREADABLE_REQUEST_BODY_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenMoreThanOneKeyValuePairMapInBody() throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING,
                    PROMO_CODE_KEYWORD_KEY, PROMO_CODE_DEFAULT_KEYWORD);

            String expectedContent = MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBodyContainsNoPromoCodeKeywordKey() throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING);

            String expectedContent = PATCH_SHOULD_CONTAIN_KEY_MESSAGE_TEMPLATE_PREFIX + PROMO_CODE_KEYWORD_KEY;

            mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#blankStrings")
        void shouldReturn400_whenPromoCodeKeywordValueIsBlank(String value) throws Exception {
            Map<String, String> requestDto = Collections.singletonMap(PROMO_CODE_KEYWORD_KEY, value);

            String expectedContent = PATCH_PROMO_CODE_KEYWORD_BlANK_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class ChangePaymentMethod {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, RIDE_DEFAULT_PAYMENT_METHOD_STRING);

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, RIDE_DEFAULT_PAYMENT_METHOD_STRING);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideId() throws Exception {
            Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, RIDE_DEFAULT_PAYMENT_METHOD_STRING);

            when(rideService.changePaymentMethod(anyString(), anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            String existingActivePromoCodeKeyword = PROMO_CODE_DEFAULT_KEYWORD;
            Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, existingActivePromoCodeKeyword);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.changePaymentMethod(anyString(), anyString()))
                    .thenReturn(responseDto);

            String existingRideId = RIDE_DEFAULT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, existingRideId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).changePaymentMethod(existingRideId, existingActivePromoCodeKeyword);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            String existingActivePromoCodeKeyword = PROMO_CODE_DEFAULT_KEYWORD;
            Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, existingActivePromoCodeKeyword);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.changePaymentMethod(anyString(), anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
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
            Map<String, String> requestDto = null;

            String expectedContent = UNREADABLE_REQUEST_BODY_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenMoreThanOneKeyValuePairMapInBody() throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING,
                    PAYMENT_METHOD_KEY, RIDE_DEFAULT_PAYMENT_METHOD_STRING);

            String expectedContent = MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBodyContainsNoPaymentMethodKey() throws Exception {
            Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, RIDE_DEFAULT_ID_STRING);

            String expectedContent = PATCH_SHOULD_CONTAIN_KEY_MESSAGE_TEMPLATE_PREFIX + PAYMENT_METHOD_KEY;

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenInvalidPaymentMethod() throws Exception {
            Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, RIDE_DEFAULT_ID_STRING);

            when(rideService.changePaymentMethod(anyString(), anyString()))
                    .thenThrow(IllegalArgumentException.class);

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#blankStrings")
        void shouldReturn400_whenPaymentMethodValueIsBlank(String value) throws Exception {
            Map<String, String> requestDto = Collections.singletonMap(PAYMENT_METHOD_KEY, value);

            String expectedContent = PATCH_PAYMENT_METHOD_BlANK_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

    }

    @Nested
    class ScoreDriver {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, RIDE_DEFAULT_DRIVER_SCORE);

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, RIDE_DEFAULT_DRIVER_SCORE);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideId() throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, RIDE_DEFAULT_DRIVER_SCORE);

            when(rideService.scoreDriver(anyString(), anyInt()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            Integer driverScore = RIDE_DEFAULT_DRIVER_SCORE;
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, driverScore);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.scoreDriver(anyString(), anyInt()))
                    .thenReturn(responseDto);

            String existingRideId = RIDE_DEFAULT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, existingRideId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).scoreDriver(existingRideId, driverScore);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            Integer driverScore = RIDE_DEFAULT_DRIVER_SCORE;
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, driverScore);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.scoreDriver(anyString(), anyInt()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
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
            Map<String, Integer> requestDto = null;

            String expectedContent = UNREADABLE_REQUEST_BODY_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenDriverScoreValueIsNull() throws Exception {
            Map<String, Integer> requestDto = Collections.singletonMap(DRIVER_SCORE_KEY, null);

            String expectedContent = PATCH_SCORE_NULL_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenDriverScoreValueIsMoreThanMaxAllowed() throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, AppConstants.MAX_SCORE + 1);

            String expectedContent = PATCH_SCORE_GREATER_THAN_MAX_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenDriverScoreValueIsNegative() throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, NEGATIVE_SCORE);

            String expectedContent = PATCH_SCORE_NEGATIVE_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenMoreThanOneKeyValuePairMapInBody() throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, RIDE_DEFAULT_DRIVER_SCORE, PASSENGER_SCORE_KEY,
                    RIDE_DEFAULT_PASSENGER_SCORE);

            String expectedContent = MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBodyContainsNoDriverScoreKey() throws Exception {
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, RIDE_DEFAULT_PASSENGER_SCORE);

            String expectedContent = PATCH_SHOULD_CONTAIN_KEY_MESSAGE_TEMPLATE_PREFIX + DRIVER_SCORE_KEY;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class ScorePassenger {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, RIDE_DEFAULT_PASSENGER_SCORE);

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, RIDE_DEFAULT_PASSENGER_SCORE);

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingRideId() throws Exception {
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, RIDE_DEFAULT_PASSENGER_SCORE);

            when(rideService.scorePassenger(anyString(), anyInt()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            Integer driverScore = RIDE_DEFAULT_PASSENGER_SCORE;
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, driverScore);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.scorePassenger(anyString(), anyInt()))
                    .thenReturn(responseDto);

            String existingRideId = RIDE_DEFAULT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, existingRideId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).scorePassenger(existingRideId, driverScore);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidRide_whenValidInput() throws Exception {
            Integer driverScore = RIDE_DEFAULT_PASSENGER_SCORE;
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, driverScore);
            RideResponseDto responseDto = getAcceptedRideResponseDto().build();

            when(rideService.scorePassenger(anyString(), anyInt()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
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
            Map<String, Integer> requestDto = null;

            String expectedContent = UNREADABLE_REQUEST_BODY_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenPassengerScoreValueIsNull() throws Exception {
            Map<String, Integer> requestDto = Collections.singletonMap(PASSENGER_SCORE_KEY, null);

            String expectedContent = PATCH_SCORE_NULL_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenPassengerScoreValueIsMoreThanMaxAllowed() throws Exception {
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, AppConstants.MAX_SCORE + 1);

            String expectedContent = PATCH_SCORE_GREATER_THAN_MAX_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenPassengerScoreValueIsNegative() throws Exception {
            Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, NEGATIVE_SCORE);

            String expectedContent = PATCH_SCORE_NEGATIVE_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenMoreThanOneKeyValuePairMapInBody() throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, RIDE_DEFAULT_DRIVER_SCORE, PASSENGER_SCORE_KEY,
                    RIDE_DEFAULT_PASSENGER_SCORE);

            String expectedContent = MORE_THAN_ONE_KEY_VALUE_PAIR_MESSAGE;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBodyContainsNoPassengerScoreKey() throws Exception {
            Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, RIDE_DEFAULT_DRIVER_SCORE);

            String expectedContent = PATCH_SHOULD_CONTAIN_KEY_MESSAGE_TEMPLATE_PREFIX + PASSENGER_SCORE_KEY;

            mockMvc.perform(patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class DeleteRide {

        @Test
        void shouldReturn204_whenValidInput() throws Exception {
            mockMvc.perform(delete(URL_RIDES_ID_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            doThrow(EntityNotFoundException.class).when(rideService)
                    .delete(anyString());

            mockMvc.perform(delete(URL_RIDES_ID_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(delete(URL_RIDES_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            doNothing().when(rideService)
                    .delete(anyString());

            String existingId = RIDE_DEFAULT_ID_STRING;
            mockMvc.perform(delete(URL_RIDES_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(rideService, times(1)).delete(existingId);
        }
    }

    @Nested
    class GetRideById {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(get(URL_RIDES_ID_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_RIDES_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(rideService.getRideById(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_RIDES_ID_TEMPLATE, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidRide_whenExistingId() throws Exception {
            String existingId = RIDE_DEFAULT_ID_STRING;
            RideResponseDto responseDto = getBookedRideResponseDto().build();

            when(rideService.getRideById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_ID_TEMPLATE, existingId)
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
            String existingId = RIDE_DEFAULT_ID_STRING;
            RideResponseDto responseDto = getBookedRideResponseDto().build();

            when(rideService.getRideById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).getRideById(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RideResponseDto result = objectMapper.readValue(actualResponseBody, RideResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class GetRides {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();

            when(rideService.getRides(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidRides_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();

            when(rideService.getRides(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES)
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
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(rideService.getRides(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).getRides(pageable, Collections.emptyMap());

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<RideResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<RideResponseDto>>() {
                    });

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(IN_LIST_CONTAINER_TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturn400_whenNotAllowedRequestParam() throws Exception {
            mockMvc.perform(get(URL_RIDES_PARAM_VALUE_TEMPLATE, NOT_ALLOWED_REQUEST_PARAM, RIDE_DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400_whenBlankRequestParamValue() throws Exception {
            mockMvc.perform(get(URL_RIDES_PARAM_VALUE_TEMPLATE, DRIVER_ID_REQUEST_PARAM, EMPTY_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedDates")
        void shouldReturn400_whenNotParseableDateForDateTypeRequestParam(String value) throws Exception {
            String expectedContent = UNPARSEABLE_DATE_MESSAGE;

            mockMvc.perform(get(URL_RIDES_PARAM_VALUE_TEMPLATE, BOOK_RIDE_REQUEST_PARAM, value)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenNotParseableUUIDForUUIDTypeRequestParam(String value) throws Exception {
            String expectedContent = UNPARSEABLE_UUID_MESSAGE;

            mockMvc.perform(get(URL_RIDES_PARAM_VALUE_TEMPLATE, DRIVER_ID_REQUEST_PARAM, value)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class GetRidesByPassengerId {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();

            when(rideService.getRidesByPassengerId(anyString(), any(Pageable.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_RIDES_PASSENGER_ID_TEMPLATE, RIDE_DEFAULT_PASSENGER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_RIDES_PASSENGER_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturnValidRides_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();

            when(rideService.getRidesByPassengerId(anyString(), any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_PASSENGER_ID_TEMPLATE, RIDE_DEFAULT_PASSENGER_ID_STRING)
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
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(rideService.getRidesByPassengerId(anyString(), any(Pageable.class)))
                    .thenReturn(responseDto);

            String existingPassengerId = RIDE_DEFAULT_PASSENGER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_PASSENGER_ID_TEMPLATE, existingPassengerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).getRidesByPassengerId(existingPassengerId, pageable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<RideResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<RideResponseDto>>() {
                    });

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(IN_LIST_CONTAINER_TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class GetRidesByDriverId {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();

            when(rideService.getRidesByDriverId(anyString(), any(Pageable.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_RIDES_DRIVER_ID_TEMPLATE, RIDE_DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_RIDES_DRIVER_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturnValidRides_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();

            when(rideService.getRidesByDriverId(anyString(), any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_DRIVER_ID_TEMPLATE, RIDE_DEFAULT_DRIVER_ID_STRING)
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
            ListContainerResponseDto<RideResponseDto> responseDto = getRideResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(rideService.getRidesByDriverId(anyString(), any(Pageable.class)))
                    .thenReturn(responseDto);

            String existingDriverId = RIDE_DEFAULT_DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_DRIVER_ID_TEMPLATE, existingDriverId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(rideService, times(1)).getRidesByDriverId(existingDriverId, pageable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<RideResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<RideResponseDto>>() {
                    });

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(IN_LIST_CONTAINER_TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class GetPassengerRating {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            RatingResponseDto responseDto = getPassengerRating().build();

            when(ratingService.getPassengerRating(anyString()))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_RIDES_PASSENGER_ID_RATING_TEMPLATE, RIDE_DEFAULT_PASSENGER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_RIDES_PASSENGER_ID_RATING_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNoScoresFound() throws Exception {
            when(ratingService.getPassengerRating(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_RIDES_PASSENGER_ID_RATING_TEMPLATE, RIDE_DEFAULT_PASSENGER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidRide_whenExistingId() throws Exception {
            RatingResponseDto responseDto = getPassengerRating().build();

            when(ratingService.getPassengerRating(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc
                    .perform(get(URL_RIDES_PASSENGER_ID_RATING_TEMPLATE, RIDE_DEFAULT_PASSENGER_ID_STRING)
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
            RatingResponseDto responseDto = getPassengerRating().build();

            when(ratingService.getPassengerRating(anyString()))
                    .thenReturn(responseDto);

            String existingId = RIDE_DEFAULT_PASSENGER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_PASSENGER_ID_RATING_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(ratingService, times(1)).getPassengerRating(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RatingResponseDto result = objectMapper.readValue(actualResponseBody, RatingResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class GetDriverRating {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            RatingResponseDto responseDto = getDriverRating().build();

            when(ratingService.getDriverRating(anyString()))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_RIDES_DRIVER_ID_RATING_TEMPLATE, RIDE_DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.RideControllerTest#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_RIDES_DRIVER_ID_RATING_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNoScoresFound() throws Exception {
            when(ratingService.getDriverRating(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_RIDES_DRIVER_ID_RATING_TEMPLATE, RIDE_DEFAULT_DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidRide_whenExistingId() throws Exception {
            RatingResponseDto responseDto = getDriverRating().build();

            when(ratingService.getDriverRating(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc
                    .perform(get(URL_RIDES_DRIVER_ID_RATING_TEMPLATE, RIDE_DEFAULT_DRIVER_ID_STRING)
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
            RatingResponseDto responseDto = getDriverRating().build();

            when(ratingService.getDriverRating(anyString()))
                    .thenReturn(responseDto);

            String existingId = RIDE_DEFAULT_DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_RIDES_DRIVER_ID_RATING_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(ratingService, times(1)).getDriverRating(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            RatingResponseDto result = objectMapper.readValue(actualResponseBody, RatingResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELDS)
                    .isEqualTo(responseDto);
        }
    }

    static Stream<String> blankStrings() {
        return Stream.of("", "   ", null);
    }

    static Stream<String> malformedUUIDs() {
        return Stream.of("3abcc6a1-94da-4185-1-8a11c1b8efd2", "3abcc6a1-94da-4185-aaa1-8a11c1b8efdw",
                "3ABCC6A1-94DA-4185-AAA1-8A11C1B8EFD2", "   ", "1234", "abc", "111122223333444a",
                "111122223333-444");
    }

    static Stream<String> malformedDates() {
        return Stream.of("", "   ", "abc", "111122223333444a", "111122223333-444", "truth", "lies", "trui",
                "falce", "232", "2004-18", "23-121", "2004-12-88", "2004-11-11T25", "2004-11-11 11");
    }
}
