package by.arvisit.cabapp.driverservice.service.impl;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.COLOR_ID_WHITE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.COLOR_NAME_WHITE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getColor;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getColorResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getColorResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.mapper.ColorMapper;
import by.arvisit.cabapp.driverservice.persistence.model.Color;
import by.arvisit.cabapp.driverservice.persistence.repository.ColorRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ColorServiceImplTest {

    @InjectMocks
    private ColorServiceImpl colorService;
    @Mock
    private ColorRepository colorRepository;
    @Mock
    private ColorMapper colorMapper;
    @Mock
    private MessageSource messageSource;

    @Test
    void shouldReturnEntity_whenGetColorEntityByExistingId() {
        Color color = getColor().build();

        when(colorRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(color));

        Color actualColor = colorService.getColorEntityById(COLOR_ID_WHITE);

        assertThat(actualColor.getId())
                .isEqualTo(COLOR_ID_WHITE);
        assertThat(actualColor.getName())
                .isEqualTo(COLOR_NAME_WHITE);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetColorEntityByNonExistingId() {
        when(colorRepository.findById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> colorService.getColorEntityById(COLOR_ID_WHITE))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnContainerWithColorResponseDto_whenGetColors() {
        List<Color> colors = List.of(getColor().build());
        Page<Color> colorsPage = new PageImpl<>(colors);
        ColorResponseDto colorResponseDto = getColorResponseDto().build();

        when(colorRepository.findAll(any(Pageable.class)))
                .thenReturn(colorsPage);
        when(colorMapper.fromEntityToResponseDto(any(Color.class)))
                .thenReturn(colorResponseDto);

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        assertThat(colorService.getColors(pageable))
                .isEqualTo(getColorResponseDtoInListContainer().build());
    }

    @Test
    void shouldReturnTrue_whenColorExists() {
        when(colorRepository.existsById(any(Integer.class)))
                .thenReturn(true);

        assertThat(colorService.existsById(COLOR_ID_WHITE))
                .isTrue();
    }

    @Test
    void shouldReturnFalse_whenColorDoesNotExist() {
        when(colorRepository.existsById(any(Integer.class)))
                .thenReturn(false);

        assertThat(colorService.existsById(COLOR_ID_WHITE))
                .isFalse();
    }

}
