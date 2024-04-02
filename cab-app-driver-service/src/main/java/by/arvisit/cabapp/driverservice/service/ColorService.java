package by.arvisit.cabapp.driverservice.service;

import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Color;

public interface ColorService {

    Color getColorEntityById(Integer id);

    ListContainerResponseDto<ColorResponseDto> getColors();

    boolean existsById(Integer id);
}
