package by.arvisit.cabapp.driverservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Color;

public interface ColorService {

    Color getColorEntityById(Integer id);

    ListContainerResponseDto<ColorResponseDto> getColors(Pageable pageable);

    boolean existsById(Integer id);
}
