package by.arvisit.cabapp.paymentservice.controller;

import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DRIVER_ID_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DRIVER_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DRIVER_PAYMENT_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.NOT_ALLOWED_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.NOT_VALID_OPERATION;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_PAYMENT_NEGATIVE_AMOUNT;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.TIMESTAMP_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.URL_DRIVER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.URL_DRIVER_PAYMENTS_ID_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.URL_DRIVER_PAYMENTS_PARAM_VALUE_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverAccountBalanceResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getListContainerForResponse;
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
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.service.DriverPaymentService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = DriverPaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class DriverPaymentControllerTest {

    private static final String UNPARSEABLE_DATE_MESSAGE = "Unparseable date values detected";
    private static final String UNPARSEABLE_UUID_MESSAGE = "Unparseable UUID values detected";
    private static final String REQUEST_PARAMS_VALUE_BLANK_MESSAGE = "must not be blank";
    private static final String REQUEST_PARAMS_NOT_ALLOWED_MESSAGE = "Invalid input parameters: noSuchParam. Valid parameters are: size, driverId, page, sort, operation, status, timestamp";
    private static final String OPERATION_TYPE_NOT_VALID_MESSAGE = "Operation type should be WITHDRAWAL or REPAYMENT";
    private static final String OPERATION_BLANK_MESSAGE = "Operation should not be blank";
    private static final String CARD_NUMBER_NOT_MATCH_PATTERN = "Card number should consist of 16 digits";
    private static final String CARD_NUMBER_BLANK_MESSAGE = "Card number should not be blank";
    private static final String PAYMENT_AMOUNT_NEGATIVE_MESSAGE = "Payment amount value should be greater than 0";
    private static final String PAYMENT_AMOUNT_NULL_MESSAGE = "Payment amount value should not be null";
    private static final String DRIVER_ID_NULL_MESSAGE = "Driver id should not be null";
    private static final String MALFORMED_UUID_MESSAGE = "must be a valid UUID";
    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String IN_LIST_CONTAINER_TIMESTAMP_FIELD = "values.timestamp";
    private static final String EMPTY_STRING = "";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DriverPaymentService driverPaymentService;

    @Nested
    class SaveDriverPayment {

        @Test
        void shouldReturn201_whenValidInput() throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto().build();

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            DriverPaymentResponseDto responseDto = getDriverPaymentResponseDto().build();
            when(driverPaymentService.save(any(DriverPaymentRequestDto.class)))
                    .thenReturn(responseDto);

            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto().build();

            MvcResult mvcResult = mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            verify(driverPaymentService, times(1)).save(requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverPaymentResponseDto result = objectMapper.readValue(actualResponseBody,
                    DriverPaymentResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELD)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPayment_whenValidInput() throws Exception {
            DriverPaymentResponseDto responseDto = getDriverPaymentResponseDto().build();
            when(driverPaymentService.save(any(DriverPaymentRequestDto.class)))
                    .thenReturn(responseDto);

            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto().build();

            MvcResult mvcResult = mockMvc.perform(post(URL_DRIVER_PAYMENTS)
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
            when(driverPaymentService.save(any(DriverPaymentRequestDto.class)))
                    .thenThrow(IllegalStateException.class);

            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto().build();

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }

        @Test
        void shouldReturn400_whenNullDriverId() throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withDriverId(null)
                    .build();

            String expectedContent = DRIVER_ID_NULL_MESSAGE;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
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
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withDriverId(driverId)
                    .build();

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullAmount() throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withAmount(null)
                    .build();

            String expectedContent = PAYMENT_AMOUNT_NULL_MESSAGE;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNegativeAmount() throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withAmount(PASSENGER_PAYMENT_NEGATIVE_AMOUNT)
                    .build();

            String expectedContent = PAYMENT_AMOUNT_NEGATIVE_MESSAGE;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#blankStrings")
        void shouldReturn400_whenBlankCardNumber(String cardNumber) throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withCardNumber(cardNumber)
                    .build();

            String expectedContent = CARD_NUMBER_BLANK_MESSAGE;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
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
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withCardNumber(cardNumber)
                    .build();

            String expectedContent = CARD_NUMBER_NOT_MATCH_PATTERN;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.paymentservice.util.PaymentTestData#blankStrings")
        void shouldReturn400_whenBlankOperation(String operation) throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withOperation(operation)
                    .build();

            String expectedContent = OPERATION_BLANK_MESSAGE;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNotValidOperation() throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withOperation(NOT_VALID_OPERATION)
                    .build();

            String expectedContent = OPERATION_TYPE_NOT_VALID_MESSAGE;

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @EnumSource(OperationTypeEnum.class)
        void shouldReturn201_whenValidOperation(OperationTypeEnum operation) throws Exception {
            DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                    .withOperation(operation.toString())
                    .build();

            mockMvc.perform(post(URL_DRIVER_PAYMENTS)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    class GetPaymentById {

        @Test
        void shouldReturn200_whenExistingId() throws Exception {
            mockMvc.perform(get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, DRIVER_PAYMENT_ID_STRING)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            DriverPaymentResponseDto responseDto = getDriverPaymentResponseDto().build();

            when(driverPaymentService.getPaymentById(anyString()))
                    .thenReturn(responseDto);

            String paymentId = DRIVER_PAYMENT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, paymentId)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverPaymentService, times(1)).getPaymentById(paymentId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverPaymentResponseDto result = objectMapper.readValue(actualResponseBody,
                    DriverPaymentResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(TIMESTAMP_FIELD)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPayment_whenExistingId() throws Exception {
            DriverPaymentResponseDto responseDto = getDriverPaymentResponseDto().build();

            when(driverPaymentService.getPaymentById(anyString()))
                    .thenReturn(responseDto);

            String paymentId = DRIVER_PAYMENT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, paymentId)
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

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, id)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(driverPaymentService.getPaymentById(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, DRIVER_PAYMENT_ID_STRING)
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
            mockMvc.perform(get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, DRIVER_PAYMENT_ID_STRING)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<DriverPaymentResponseDto> responseDto = getListContainerForResponse(
                    DriverPaymentResponseDto.class)
                    .withValues(List.of(getDriverPaymentResponseDto().build()))
                    .build();

            when(driverPaymentService.getPayments(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVER_PAYMENTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            verify(driverPaymentService, times(1)).getPayments(pageable, Collections.emptyMap());

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<DriverPaymentResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<DriverPaymentResponseDto>>() {
                    });

            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields(IN_LIST_CONTAINER_TIMESTAMP_FIELD)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPayments_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<DriverPaymentResponseDto> responseDto = getListContainerForResponse(
                    DriverPaymentResponseDto.class)
                    .withValues(List.of(getDriverPaymentResponseDto().build()))
                    .build();

            when(driverPaymentService.getPayments(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVER_PAYMENTS)
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

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_PARAM_VALUE_TEMPLATE, NOT_ALLOWED_REQUEST_PARAM, DRIVER_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenBlankRequestParamValue() throws Exception {
            String expectedContent = REQUEST_PARAMS_VALUE_BLANK_MESSAGE;

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_PARAM_VALUE_TEMPLATE, DRIVER_ID_REQUEST_PARAM, EMPTY_STRING)
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

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_PARAM_VALUE_TEMPLATE, TIMESTAMP_REQUEST_PARAM, value)
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

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_PARAM_VALUE_TEMPLATE, DRIVER_ID_REQUEST_PARAM, value)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }
    }

    @Nested
    class GetDriverAccountBalance {

        @Test
        void shouldReturn200_whenExistingId() throws Exception {
            DriverAccountBalanceResponseDto responseDto = getDriverAccountBalanceResponseDto().build();

            when(driverPaymentService.getDriverAccountBalance(anyString()))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, DRIVER_ID_STRING)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            DriverAccountBalanceResponseDto responseDto = getDriverAccountBalanceResponseDto().build();

            when(driverPaymentService.getDriverAccountBalance(anyString()))
                    .thenReturn(responseDto);

            String driverId = DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, driverId)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(driverPaymentService, times(1)).getDriverAccountBalance(driverId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            DriverAccountBalanceResponseDto result = objectMapper.readValue(actualResponseBody,
                    DriverAccountBalanceResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPayment_whenExistingId() throws Exception {
            DriverAccountBalanceResponseDto responseDto = getDriverAccountBalanceResponseDto().build();

            when(driverPaymentService.getDriverAccountBalance(anyString()))
                    .thenReturn(responseDto);

            String driverId = DRIVER_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, driverId)
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
            String expectedMessage = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, id)
                    .characterEncoding(ENCODING_UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedMessage)));
        }
    }
}
