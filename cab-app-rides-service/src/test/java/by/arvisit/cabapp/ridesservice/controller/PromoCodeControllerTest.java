package by.arvisit.cabapp.ridesservice.controller;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_DEFAULT_ID;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_PROMO_CODES;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_PROMO_CODES_ACTIVE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.URL_PROMO_CODES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import by.arvisit.cabapp.exceptionhandlingstarter.exception.ValueAlreadyInUseException;
import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.service.PromoCodeService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = PromoCodeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class PromoCodeControllerTest {

    private static final int DISCOUNT_PERCENT_MAX_ALLOWED = 100;
    private static final int DISCOUNT_PERCENT_MIN_ALLOWED = 0;
    private static final int DISCOUNT_PERCENT_101 = 101;
    private static final int NEGATIVE_DISCOUNT_PERCENT = -1;
    private static final String NEGATIVE_DISCOUNT_PERCENT_MESSAGE = "Promo code discount should have positive or zero value";
    private static final String DISCOUNT_PERCENT_GREATER_THAN_MAX_MESSAGE = "Promo code discount should not be greater than "
            + DISCOUNT_PERCENT_MAX_ALLOWED;
    private static final String NOT_NULL_DISCOUNT_PERCENT_MESSAGE = "Promo code discount should not be null";
    private static final String SIZE_KEYWORD_MESSAGE = "Promo code keyword should not be longer than 20";
    private static final String NOT_BLANK_KEYWORD_MESSAGE = "Promo code keyword should not be blank";
    private static final String LETTERS_101 = "TVQDfZXYIzFNzhzskpZiwULzGOikAuVtwRPnYaCjQjdeMJKcEbvpuiQNUmaERmPtyDwFHPHaiHnWZHmoXrRNUVDWVOkZFhlVOBuMC";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PromoCodeService promoCodeService;

    @Nested
    class SavePromoCode {

        @Test
        void shouldReturn201_whenValidInput() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto().build();

            when(promoCodeService.save(any(PromoCodeRequestDto.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            verify(promoCodeService, times(1)).save(requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PromoCodeResponseDto result = objectMapper.readValue(actualResponseBody, PromoCodeResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPromoCode_whenValidInput() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto().build();

            when(promoCodeService.save(any(PromoCodeRequestDto.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(post(URL_PROMO_CODES)
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
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.PromoCodeControllerTest#blankStrings")
        void shouldReturn400_whenBlankKeyword(String keyword) throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withKeyword(keyword)
                    .build();

            String expectedContent = NOT_BLANK_KEYWORD_MESSAGE;

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenKeywordLongerThanMaxAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withKeyword(LETTERS_101)
                    .build();

            String expectedContent = SIZE_KEYWORD_MESSAGE;

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullDiscountPercent() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(null)
                    .build();

            String expectedContent = NOT_NULL_DISCOUNT_PERCENT_MESSAGE;

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNegativeDiscountPercent() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(NEGATIVE_DISCOUNT_PERCENT)
                    .build();

            String expectedContent = NEGATIVE_DISCOUNT_PERCENT_MESSAGE;

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenDiscountPercentGreaterThanMaxAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(DISCOUNT_PERCENT_101)
                    .build();

            String expectedContent = DISCOUNT_PERCENT_GREATER_THAN_MAX_MESSAGE;

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn201_whenDiscountPercentEqualsMaxAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(DISCOUNT_PERCENT_MAX_ALLOWED)
                    .build();

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturn201_whenDiscountPercentEqualsMinAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(DISCOUNT_PERCENT_MIN_ALLOWED)
                    .build();

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturn409_whenKeywordInActiveUse() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();

            when(promoCodeService.save(any(PromoCodeRequestDto.class)))
                    .thenThrow(ValueAlreadyInUseException.class);

            mockMvc.perform(post(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class UpdatePromoCode {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();

            when(promoCodeService.update(anyLong(), any(PromoCodeRequestDto.class)))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto().build();

            when(promoCodeService.update(anyLong(), any(PromoCodeRequestDto.class)))
                    .thenReturn(responseDto);

            Long existingId = PROMO_CODE_DEFAULT_ID;
            MvcResult mvcResult = mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(promoCodeService, times(1)).update(existingId, requestDto);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PromoCodeResponseDto result = objectMapper.readValue(actualResponseBody, PromoCodeResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPromoCode_whenValidInput() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto().build();

            when(promoCodeService.update(anyLong(), any(PromoCodeRequestDto.class)))
                    .thenReturn(responseDto);

            Long existingId = PROMO_CODE_DEFAULT_ID;
            MvcResult mvcResult = mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, existingId)
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
        @MethodSource("by.arvisit.cabapp.ridesservice.controller.PromoCodeControllerTest#blankStrings")
        void shouldReturn400_whenBlankKeyword(String keyword) throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withKeyword(keyword)
                    .build();

            String expectedContent = NOT_BLANK_KEYWORD_MESSAGE;

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenKeywordLongerThanMaxAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withKeyword(LETTERS_101)
                    .build();

            String expectedContent = SIZE_KEYWORD_MESSAGE;

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNullDiscountPercent() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(null)
                    .build();

            String expectedContent = NOT_NULL_DISCOUNT_PERCENT_MESSAGE;

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenNegativeDiscountPercent() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(NEGATIVE_DISCOUNT_PERCENT)
                    .build();

            String expectedContent = NEGATIVE_DISCOUNT_PERCENT_MESSAGE;

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn400_whenDiscountPercentGreaterThanMaxAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(DISCOUNT_PERCENT_101)
                    .build();

            String expectedContent = DISCOUNT_PERCENT_GREATER_THAN_MAX_MESSAGE;

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString(expectedContent)));
        }

        @Test
        void shouldReturn200_whenDiscountPercentEqualsMaxAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(DISCOUNT_PERCENT_MAX_ALLOWED)
                    .build();

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturn200_whenDiscountPercentEqualsMinAllowed() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                    .withDiscountPercent(DISCOUNT_PERCENT_MIN_ALLOWED)
                    .build();

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturn409_whenKeywordInActiveUse() throws Exception {
            PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();

            when(promoCodeService.update(anyLong(), any(PromoCodeRequestDto.class)))
                    .thenThrow(ValueAlreadyInUseException.class);

            mockMvc.perform(put(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class Deactivate {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(patch(URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(promoCodeService.deactivatePromoCode(anyLong()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(patch(URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenValidInput() throws Exception {
            boolean newIsActive = false;
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto()
                    .withIsActive(newIsActive)
                    .build();

            when(promoCodeService.deactivatePromoCode(anyLong()))
                    .thenReturn(responseDto);

            Long existingId = PROMO_CODE_DEFAULT_ID;
            MvcResult mvcResult = mockMvc.perform(patch(URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(promoCodeService, times(1)).deactivatePromoCode(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PromoCodeResponseDto result = objectMapper.readValue(actualResponseBody, PromoCodeResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(responseDto);
        }

        @Test
        void shouldReturnValidPromoCode_whenValidInput() throws Exception {
            boolean newIsActive = false;
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto()
                    .withIsActive(newIsActive)
                    .build();

            when(promoCodeService.deactivatePromoCode(anyLong()))
                    .thenReturn(responseDto);

            Long existingId = PROMO_CODE_DEFAULT_ID;
            MvcResult mvcResult = mockMvc.perform(patch(URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponseBody = mvcResult.getResponse().getContentAsString();

            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(responseDto));
        }
    }

    @Nested
    class DeletePromoCode {

        @Test
        void shouldReturn204_whenValidInput() throws Exception {
            mockMvc.perform(delete(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            doThrow(EntityNotFoundException.class).when(promoCodeService)
                    .delete(anyLong());

            mockMvc.perform(delete(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldMapToBusinessModel_whenExistingId() throws Exception {
            doNothing().when(promoCodeService)
                    .delete(anyLong());

            Long existingId = PROMO_CODE_DEFAULT_ID;
            mockMvc.perform(delete(URL_PROMO_CODES_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(promoCodeService, times(1)).delete(existingId);
        }
    }

    @Nested
    class GetPromoCodeById {

        @Test
        void shouldReturn200_whenValidInput() throws Exception {
            mockMvc.perform(get(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturn404_whenNonExistingId() throws Exception {
            when(promoCodeService.getPromoCodeById(anyLong()))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(get(URL_PROMO_CODES_ID_TEMPLATE, PROMO_CODE_DEFAULT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnValidPromoCode_whenExistingId() throws Exception {
            Long existingId = PROMO_CODE_DEFAULT_ID;
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto().build();

            when(promoCodeService.getPromoCodeById(anyLong()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PROMO_CODES_ID_TEMPLATE, existingId)
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
            Long existingId = PROMO_CODE_DEFAULT_ID;
            PromoCodeResponseDto responseDto = getPromoCodeResponseDto().build();

            when(promoCodeService.getPromoCodeById(anyLong()))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PROMO_CODES_ID_TEMPLATE, existingId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(promoCodeService, times(1)).getPromoCodeById(existingId);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            PromoCodeResponseDto result = objectMapper.readValue(actualResponseBody, PromoCodeResponseDto.class);

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class GetPromoCodes {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PromoCodeResponseDto> responseDto = getPromoCodeResponseDtoInListContainer()
                    .build();

            when(promoCodeService.getPromoCodes(any(Pageable.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidPromoCodes_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PromoCodeResponseDto> responseDto = getPromoCodeResponseDtoInListContainer()
                    .build();

            when(promoCodeService.getPromoCodes(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PROMO_CODES)
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
            ListContainerResponseDto<PromoCodeResponseDto> responseDto = getPromoCodeResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(promoCodeService.getPromoCodes(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PROMO_CODES)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(promoCodeService, times(1)).getPromoCodes(pageable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<PromoCodeResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<PromoCodeResponseDto>>() {
                    });

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(responseDto);
        }
    }

    @Nested
    class GetActivePromoCodes {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PromoCodeResponseDto> responseDto = getPromoCodeResponseDtoInListContainer()
                    .build();

            when(promoCodeService.getActivePromoCodes(any(Pageable.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_PROMO_CODES_ACTIVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidPromoCodes_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<PromoCodeResponseDto> responseDto = getPromoCodeResponseDtoInListContainer()
                    .build();

            when(promoCodeService.getActivePromoCodes(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PROMO_CODES_ACTIVE)
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
            ListContainerResponseDto<PromoCodeResponseDto> responseDto = getPromoCodeResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(promoCodeService.getActivePromoCodes(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_PROMO_CODES_ACTIVE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(promoCodeService, times(1)).getActivePromoCodes(pageable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<PromoCodeResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<PromoCodeResponseDto>>() {
                    });

            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(responseDto);
        }
    }

    static Stream<String> blankStrings() {
        return Stream.of("", "   ", null);
    }

}
