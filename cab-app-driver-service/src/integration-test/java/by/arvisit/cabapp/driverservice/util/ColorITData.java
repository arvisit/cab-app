package by.arvisit.cabapp.driverservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Color;

public class ColorITData {

    private ColorITData() {
    }

    private static final Map<Integer, String> COLOR_DATA;
    public static final List<Color> COLORS = new ArrayList<>();
    public static final List<ColorResponseDto> COLOR_RESPONSE_DTOS = new ArrayList<>();

    static {
        COLOR_DATA = Map.of(1, "White", 2, "Black", 3, "Gray", 4, "Silver", 5, "Blue", 6, "Red", 7, "Brown", 8, "Green",
                9, "Orange", 10, "Beige");

        for (Map.Entry<Integer, String> entry : COLOR_DATA.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            COLORS.add(new Color(key, value));
            COLOR_RESPONSE_DTOS.add(new ColorResponseDto(key, value));
        }
        COLORS.sort((i1, i2) -> {
            if (i1.getId() < i2.getId()) {
                return -1;
            } else if (i1.getId() > i2.getId()) {
                return 1;
            } else {
                return 0;
            }
        });
        COLOR_RESPONSE_DTOS.sort((i1, i2) -> {
            if (i1.id() < i2.id()) {
                return -1;
            } else if (i1.id() > i2.id()) {
                return 1;
            } else {
                return 0;
            }
        });

    }
}
