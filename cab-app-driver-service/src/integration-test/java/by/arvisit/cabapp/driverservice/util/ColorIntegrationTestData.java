package by.arvisit.cabapp.driverservice.util;

import java.util.List;

import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;

public class ColorIntegrationTestData {

    private ColorIntegrationTestData() {
    }

    public static final List<ColorResponseDto> COLORS;

    static {
        COLORS = List.of(new ColorResponseDto(1, "White"),
                new ColorResponseDto(2, "Black"),
                new ColorResponseDto(3, "Gray"),
                new ColorResponseDto(4, "Silver"),
                new ColorResponseDto(5, "Blue"),
                new ColorResponseDto(6, "Red"),
                new ColorResponseDto(7, "Brown"),
                new ColorResponseDto(8, "Green"),
                new ColorResponseDto(9, "Orange"),
                new ColorResponseDto(10, "Beige"));
    }
}
