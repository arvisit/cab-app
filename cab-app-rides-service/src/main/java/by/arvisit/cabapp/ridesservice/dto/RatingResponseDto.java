package by.arvisit.cabapp.ridesservice.dto;

import by.arvisit.cabapp.ridesservice.persistence.model.UserTypeEnum;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record RatingResponseDto(
        UserTypeEnum userType,
        String userId,
        Double rating) {

}
