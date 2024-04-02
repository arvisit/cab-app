package by.arvisit.cabapp.driverservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.service.ColorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/colors")
@RequiredArgsConstructor
@Slf4j
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ListContainerResponseDto<ColorResponseDto> getColors() {
        ListContainerResponseDto<ColorResponseDto> response = colorService.getColors();

        log.debug("Got all colors. Total count: {}", response.values().size());
        return response;
    }
}
