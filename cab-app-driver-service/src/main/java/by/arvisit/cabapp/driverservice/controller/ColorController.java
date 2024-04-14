package by.arvisit.cabapp.driverservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.service.ColorService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/colors")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ListContainerResponseDto<ColorResponseDto> getColors(@PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<ColorResponseDto> response = colorService.getColors(pageable);

        log.debug("Got all colors. Total count: {}. Pageable settings: {}", response.values().size(), pageable);
        return response;
    }
}
