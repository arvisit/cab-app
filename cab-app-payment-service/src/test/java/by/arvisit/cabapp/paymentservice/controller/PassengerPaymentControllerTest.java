package by.arvisit.cabapp.paymentservice.controller;

import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.NOT_ALLOWED_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.NOT_VALID_PAYMENT_METHOD;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_CARD_NUMBER;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_ID_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_PAYMENT_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_PAYMENT_NEGATIVE_AMOUNT;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.TIMESTAMP_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.URL_PASSENGER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.URL_PASSENGER_PAYMENTS_ID_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.URL_PASSENGER_PAYMENTS_PARAM_VALUE_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getListContainerForResponse;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPaymentResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

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
import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.paymentservice.service.PassengerPaymentService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = PassengerPaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class PassengerPaymentControllerTest {

    private static final String UNPARSEABLE_DATE_MESSAGE = "Unparseable date values detected";
    private static final String UNPARSEABLE_UUID_MESSAGE = "Unparseable UUID values detected";
    private static final String REQUEST_PARAMS_VALUE_BLANK_MESSAGE = "must not be blank";
    private static final String REQUEST_PARAMS_NOT_ALLOWED_MESSAGE = "Only specified request parameters are allowed: [page, size, sort, status, paymentMethod, passengerId, driverId, timestamp]";
    private static final String PAYMENT_METHOD_NOT_VALID_MESSAGE = "Payment method should be BANK_CARD or CASH";
    private static final String PAYMENT_METHOD_BLANK_MESSAGE = "Payment method should not be blank";
    private static final String CARD_NUMBER_NOT_MATCH_PATTERN = "Card number should consist of 16 digits";
    private static final String PAYMENT_AMOUNT_NEGATIVE_MESSAGE = "Payment amount value should be greater than 0";
    private static final String PAYMENT_AMOUNT_NULL_MESSAGE = "Payment amount value should not be null";
    private static final String PASSENGER_ID_NULL_MESSAGE = "Passenger id should not be null";
    private static final String DRIVER_ID_NULL_MESSAGE = "Driver id should not be null";
    private static final String RIDE_ID_NULL_MESSAGE = "Ride id should not be null";
    private static final String MALFORMED_UUID_MESSAGE = "must be a valid UUID";
    private static final String PAYMENT_METHOD_CARD_BAD_COMBINATION_MESSAGE = "If payment method is CASH then card number should be null. If payment method is BANK_CARD then card number should not be null";
    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String IN_LIST_CONTAINER_TIMESTAMP_FIELD = "values.timestamp";
    private static final String EMPTY_STRING = "";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PassengerPaymentService passengerPaymentService;

    @Nested
    class SavePassengerPayment {

        @Test
        void shouldReturn201_whenValidInput() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto().build();

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            PassengerPaymentResponseDto responseDto = getPassengerPaymentResponseDto().build();
            when(passengerPaymentService.save(any(PassengerPaymentRequestDto.class)))
                    .thenReturn(responseDto);

            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto().build();

            MvcResult mvcResult = mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            verify(passengerPaymentService, times(1)).save(requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PassengerPaymentResponseDto result = objectMapper.readValue(actualResponseBody,
                    PassengerPaymentResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELD)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPayment_whenValidInput() throws Exception {
            PassengerPaymentResponseDto responseDto = getPassengerPaymentResponseDto().build();
            when(passengerPaymentService.save(any(PassengerPaymentRequestDto.class)))
                    .thenReturn(responseDto);

            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto().build();

            MvcResult mvcResult = mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @Test
        void shouldReturn409_whenNotVerifiedInput() throws Exception {
            when(passengerPaymentService.save(any(PassengerPaymentRequestDto.class)))
                    .thenThrow(IllegalStateException.class);

            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto().build();

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }

        @Test
        void shouldReturn400_whenNullPassengerId() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPassengerId(null)
                    .build();

            String expectedContent = PASSENGER_ID_NULL_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedPassengerId(String passengerId) throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPassengerId(passengerId)
                    .build();

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullDriverId() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withDriverId(null)
                    .build();

            String expectedContent = DRIVER_ID_NULL_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedDriverId(String driverId) throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withDriverId(driverId)
                    .build();

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullRideId() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withRideId(null)
                    .build();

            String expectedContent = RIDE_ID_NULL_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedRideId(String rideId) throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withRideId(rideId)
                    .build();

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullAmount() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withAmount(null)
                    .build();

            String expectedContent = PAYMENT_AMOUNT_NULL_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNegativeAmount() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withAmount(PASSENGER_PAYMENT_NEGATIVE_AMOUNT)
                    .build();

            String expectedContent = PAYMENT_AMOUNT_NEGATIVE_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#malformedCardNumbers")
        void shouldReturn400_whenCardNumberNotMatchPattern(String cardNumber) throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withCardNumber(cardNumber)
                    .build();

            String expectedContent = CARD_NUMBER_NOT_MATCH_PATTERN;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#blankStrings")
        void shouldReturn400_whenBlankPaymentMethod(String paymentMethod) throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPaymentMethod(paymentMethod)
                    .build();

            String expectedContent = PAYMENT_METHOD_BLANK_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNotValidPaymentMethod() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPaymentMethod(NOT_VALID_PAYMENT_METHOD)
                    .build();

            String expectedContent = PAYMENT_METHOD_NOT_VALID_MESSAGE;

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn201_whenBankCardPaymentMethodAndCardNumberUsed() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                    .withCardNumber(PASSENGER_CARD_NUMBER)
                    .build();

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturn201_whenCashPaymentMethodAndNoCardNumberUsed() throws Exception {
            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                    .withCardNumber(null)
                    .build();

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturn400_whenBankCardPaymentMethodAndNoCardNumberUsed() throws Exception {
            String expectedContent = PAYMENT_METHOD_CARD_BAD_COMBINATION_MESSAGE;

            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                    .withCardNumber(null)
                    .build();

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenCashPaymentMethodAndCardNumberUsed() throws Exception {
            String expectedContent = PAYMENT_METHOD_CARD_BAD_COMBINATION_MESSAGE;

            PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto()
                    .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                    .withCardNumber(PASSENGER_CARD_NUMBER)
                    .build();

            mockMvc.perform(post(URL_PASSENGER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class GetPaymentById {

        @Test
        void shouldReturn200_whenExistingId() throws Exception {
            mockMvc.perform(get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, PASSENGER_PAYMENT_ID_STRING)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            PassengerPaymentResponseDto responseDto = getPassengerPaymentResponseDto().build();

            when(passengerPaymentService.getPaymentById(anyString()))
                    .thenReturn(responseDto);

            String paymentId = PASSENGER_PAYMENT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, paymentId)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(passengerPaymentService, times(1)).getPaymentById(paymentId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PassengerPaymentResponseDto result = objectMapper.readValue(actualResponseBody,
                    PassengerPaymentResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELD)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPayment_whenExistingId() throws Exception {
            PassengerPaymentResponseDto responseDto = getPassengerPaymentResponseDto().build();

            when(passengerPaymentService.getPaymentById(anyString()))
                    .thenReturn(responseDto);

            String paymentId = PASSENGER_PAYMENT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, paymentId)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, id)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(passengerPaymentService.getPaymentById(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, PASSENGER_PAYMENT_ID_STRING)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetPayments {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            mockMvc.perform(get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, PASSENGER_PAYMENT_ID_STRING)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PassengerPaymentResponseDto> responseDto = getListContainerForResponse(
                    PassengerPaymentResponseDto.class)
                    .withValues(List.of(getPassengerPaymentResponseDto().build()))
                    .build();

            when(passengerPaymentService.getPayments(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGER_PAYMENTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            verify(passengerPaymentService, times(1)).getPayments(pageable, Collections.emptyMap());

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<PassengerPaymentResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<PassengerPaymentResponseDto>>() {
                    });

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(IN_LIST_CONTAINER_TIMESTAMP_FIELD)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPayments_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PassengerPaymentResponseDto> responseDto = getListContainerForResponse(
                    PassengerPaymentResponseDto.class)
                    .withValues(List.of(getPassengerPaymentResponseDto().build()))
                    .build();

            when(passengerPaymentService.getPayments(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGER_PAYMENTS)
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
        void shouldReturn400_whenNotAllowedRequestParam() throws Exception {
            String expectedContent = REQUEST_PARAMS_NOT_ALLOWED_MESSAGE;

            mockMvc.perform(
                    get(URL_PASSENGER_PAYMENTS_PARAM_VALUE_TEMPLATE, NOT_ALLOWED_REQUEST_PARAM, PASSENGER_ID_STRING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBlankRequestParamValue() throws Exception {
            String expectedContent = REQUEST_PARAMS_VALUE_BLANK_MESSAGE;

            mockMvc.perform(get(URL_PASSENGER_PAYMENTS_PARAM_VALUE_TEMPLATE, PASSENGER_ID_REQUEST_PARAM, EMPTY_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#malformedDates")
        void shouldReturn400_whenNotParseableDateForDateTypeRequestParam(String value) throws Exception {
            String expectedContent = UNPARSEABLE_DATE_MESSAGE;

            mockMvc.perform(get(URL_PASSENGER_PAYMENTS_PARAM_VALUE_TEMPLATE, TIMESTAMP_REQUEST_PARAM, value)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#malformedUUIDs")
        void shouldReturn400_whenNotParseableUUIDForUUIDTypeRequestParam(String value) throws Exception {
            String expectedContent = UNPARSEABLE_UUID_MESSAGE;

            mockMvc.perform(get(URL_PASSENGER_PAYMENTS_PARAM_VALUE_TEMPLATE, PASSENGER_ID_REQUEST_PARAM, value)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }
}
