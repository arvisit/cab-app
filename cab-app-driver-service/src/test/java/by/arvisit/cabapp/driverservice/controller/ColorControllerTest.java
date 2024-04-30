package by.arvisit.cabapp.driverservice.controller;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.ENCODING_UTF_8;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.URL_COLORS;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getColorResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.service.ColorService;
import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;

@WebMvcTest(controllers = ColorController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class ColorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ColorService colorService;

    @Nested
    class GetColors {

        @Test
        void shouldReturn200_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<ColorResponseDto> responseDto = getColorResponseDtoInListContainer()
                    .build();

            when(colorService.getColors(any(Pageable.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(get(URL_COLORS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnValidColors_whenNoRequestParams() throws Exception {
            ListContainerResponseDto<ColorResponseDto> responseDto = getColorResponseDtoInListContainer()
                    .build();

            when(colorService.getColors(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_COLORS)
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
            ListContainerResponseDto<ColorResponseDto> responseDto = getColorResponseDtoInListContainer()
                    .build();
            Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);

            when(colorService.getColors(any(Pageable.class)))
                    .thenReturn(responseDto);

            MvcResult mvcResult = mockMvc.perform(get(URL_COLORS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(ENCODING_UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            verify(colorService, times(1)).getColors(pageable);

            String actualResponseBody = mvcResult.getResponse().getContentAsString();
            ListContainerResponseDto<ColorResponseDto> result = objectMapper.readValue(actualResponseBody,
                    new TypeReference<ListContainerResponseDto<ColorResponseDto>>() {
                    });

            assertThat(result)
                    .isEqualTo(responseDto);
        }
    }

}
