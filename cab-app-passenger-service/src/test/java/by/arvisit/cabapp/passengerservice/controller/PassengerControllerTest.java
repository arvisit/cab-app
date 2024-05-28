package by.arvisit.cabapp.passengerservice.controller;

import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.DEFAULT_EMAIL;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.DEFAULT_ID_STRING;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NAME_REQUEST_PARAM;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NOT_ALLOWED_REQUEST_PARAM;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.URL_PASSENGERS;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.URL_PASSENGERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.URL_PASSENGERS_PARAM_VALUE_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.URL_PASSENGERS_ID_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerRequestDto;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerResponseDto;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

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
import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.service.PassengerService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = PassengerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class PassengerControllerTest {

    private static final String SIZE_NAME_MESSAGE = "Name should be no longer than 100";
    private static final String SIZE_EMAIL_MESSAGE = "Email should be no longer than 100";
    private static final String NOT_BLANK_NAME_MESSAGE = "Name should not be blank";
    private static final String NOT_BLANK_EMAIL_MESSAGE = "Email should not be blank";
    private static final String MALFORMED_EMAIL_MESSAGE = "Email should be well-formed";
    private static final String MALFORMED_UUID_MESSAGE = "must be a valid UUID";
    private static final String PATTERN_CARD_NUMBER_MESSAGE = "Card number should consist of 16 digits";
    private static final String LETTERS_101 = "TVQDfZXYIzFNzhzskpZiwULzGOikAuVtwRPnYaCjQjdeMJKcEbvpuiQNUmaERmPtyDwFHPHaiHnWZHmoXrRNUVDWVOkZFhlVOBuMC";
    private static final String EMPTY_STRING = "";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassengerService passengerService;

    @Nested
    class SavePassenger {

        @Test
        void shouldReturn201_whenValidInput() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.save(any(PassengerRequestDto.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            verify(passengerService, times(1)).save(requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PassengerResponseDto result = objectMapper.readValue(actualResponseBody, PassengerResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPassenger_whenValidInput() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.save(any(PassengerRequestDto.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(URL_PASSENGERS)
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
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#blankStrings")
        void shouldReturn400_whenBlankName(String name) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withName(name)
                    .build();

            String expectedContent = NOT_BLANK_NAME_MESSAGE;

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNameLongerThan100() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withName(LETTERS_101)
                    .build();

            String expectedContent = SIZE_NAME_MESSAGE;

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#blankStrings")
        void shouldReturn400_whenBlankEmail(String email) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withEmail(email)
                    .build();

            String expectedContent = NOT_BLANK_EMAIL_MESSAGE;

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedEmails")
        void shouldReturn400_whenMalformedEmail(String email) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withEmail(email)
                    .build();

            String expectedContent = MALFORMED_EMAIL_MESSAGE;

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenEmailLongerThan100() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withEmail(LETTERS_101)
                    .build();

            String expectedContent = SIZE_EMAIL_MESSAGE;

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedCardNumbers")
        void shouldReturn400_whenMalformedCardNumber(String cardNumber) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withCardNumber(cardNumber)
                    .build();

            String expectedContent = PATTERN_CARD_NUMBER_MESSAGE;

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn201_whenVaildInputAndNullCardNumber() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withCardNumber(null)
                    .build();

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturn409_whenEmailInUse() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();

            when(passengerService.save(any(PassengerRequestDto.class)))
                    .thenThrow(UsernameAlreadyExistsException.class);

            mockMvc.perform(post(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class UpdatePassenger {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();

            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();

            when(passengerService.update(anyString(), any(PassengerRequestDto.class)))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.update(anyString(), any(PassengerRequestDto.class)))
                    .thenReturn(responseDto);

            String existingId = DEFAULT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(passengerService, times(1)).update(existingId, requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PassengerResponseDto result = objectMapper.readValue(actualResponseBody, PassengerResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPassenger_whenValidInput() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.update(anyString(), any(PassengerRequestDto.class)))
                    .thenReturn(responseDto);

            String existingId = DEFAULT_ID_STRING;
            MvcResult mvcResult = mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, existingId)
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
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#blankStrings")
        void shouldReturn400_whenBlankName(String name) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withName(name)
                    .build();

            String expectedContent = NOT_BLANK_NAME_MESSAGE;

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNameLongerThan100() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withName(LETTERS_101)
                    .build();

            String expectedContent = SIZE_NAME_MESSAGE;

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#blankStrings")
        void shouldReturn400_whenBlankEmail(String email) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withEmail(email)
                    .build();

            String expectedContent = NOT_BLANK_EMAIL_MESSAGE;

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedEmails")
        void shouldReturn400_whenMalformedEmail(String email) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withEmail(email)
                    .build();

            String expectedContent = MALFORMED_EMAIL_MESSAGE;

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenEmailLongerThan100() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withEmail(LETTERS_101)
                    .build();

            String expectedContent = SIZE_EMAIL_MESSAGE;

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedCardNumbers")
        void shouldReturn400_whenMalformedCardNumber(String cardNumber) throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withCardNumber(cardNumber)
                    .build();

            String expectedContent = PATTERN_CARD_NUMBER_MESSAGE;

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn200_whenVaildInputAndNullCardNumber() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto()
                    .withCardNumber(null)
                    .build();

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturn409_whenEmailInUse() throws Exception {
            PassengerRequestDto requestDto = getPassengerRequestDto().build();

            when(passengerService.update(anyString(), any(PassengerRequestDto.class)))
                    .thenThrow(UsernameAlreadyExistsException.class);

            mockMvc.perform(put(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class DeletePassenger {

        @Test
        void shouldReturn204_whenValidInput() throws Exception {
            mockMvc.perform(delete(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            doThrow(EntityNotFoundException.class).when(passengerService)
                    .delete(anyString());

            mockMvc.perform(delete(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(delete(URL_PASSENGERS_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            doNothing().when(passengerService)
                    .delete(anyString());

            String existingId = DEFAULT_ID_STRING;
            mockMvc.perform(delete(URL_PASSENGERS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(passengerService, times(1)).delete(existingId);
        }
    }

    @Nested
    class GetPassengerById {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(get(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedUUIDs")
        void shouldReturn400_whenMalformedId(String id) throws Exception {
            String expectedContent = MALFORMED_UUID_MESSAGE;

            mockMvc.perform(get(URL_PASSENGERS_ID_TEMPLATE, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(passengerService.getPassengerById(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_PASSENGERS_ID_TEMPLATE, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidPassenger_whenExistingId() throws Exception {
            String existingId = DEFAULT_ID_STRING;
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.getPassengerById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGERS_ID_TEMPLATE, existingId)
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
            String existingId = DEFAULT_ID_STRING;
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.getPassengerById(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGERS_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(passengerService, times(1)).getPassengerById(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PassengerResponseDto result = objectMapper.readValue(actualResponseBody, PassengerResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

    }

    @Nested
    class GetPassengerByEmail {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(get(URL_PASSENGERS_EMAIL_TEMPLATE, DEFAULT_EMAIL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("by.arvisit.cabapp.passengerservice.util.PassengerTestData#malformedEmails")
        void shouldReturn400_whenMalformedEmail(String email) throws Exception {
            mockMvc.perform(get(URL_PASSENGERS_EMAIL_TEMPLATE, email)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn404_whenNonExistingEmail() throws Exception {
            when(passengerService.getPassengerByEmail(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_PASSENGERS_EMAIL_TEMPLATE, DEFAULT_EMAIL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidPassenger_whenExistingEmail() throws Exception {
            String existingEmail = DEFAULT_EMAIL;
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.getPassengerByEmail(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGERS_EMAIL_TEMPLATE, existingEmail)
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
            String existingEmail = DEFAULT_EMAIL;
            PassengerResponseDto responseDto = getPassengerResponseDto().build();

            when(passengerService.getPassengerByEmail(anyString()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGERS_EMAIL_TEMPLATE, existingEmail)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(passengerService, times(1)).getPassengerByEmail(existingEmail);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PassengerResponseDto result = objectMapper.readValue(actualResponseBody, PassengerResponseDto.class);

            assertThat(result)
                    .isEqualTo(responseDto);
        }

    }

    @Nested
    class GetPassengers {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PassengerResponseDto> responseDto = getPassengerResponseDtoInListContainer()
                    .build();

            when(passengerService.getPassengers(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidPassengers_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PassengerResponseDto> responseDto = getPassengerResponseDtoInListContainer()
                    .build();

            when(passengerService.getPassengers(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGERS)
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
            ListContainerResponseDto<PassengerResponseDto> responseDto = getPassengerResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(passengerService.getPassengers(any(Pageable.class), any()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PASSENGERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(passengerService, times(1)).getPassengers(pageable, Collections.emptyMap());

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<PassengerResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<PassengerResponseDto>>() {
                    });

            assertThat(result)
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturn400_whenNotAllowedRequestParam() throws Exception {
            mockMvc.perform(get(URL_PASSENGERS_PARAM_VALUE_TEMPLATE, NOT_ALLOWED_REQUEST_PARAM, DEFAULT_ID_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400_whenBlankRequestParamValue() throws Exception {
            mockMvc.perform(get(URL_PASSENGERS_PARAM_VALUE_TEMPLATE, NAME_REQUEST_PARAM, EMPTY_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
