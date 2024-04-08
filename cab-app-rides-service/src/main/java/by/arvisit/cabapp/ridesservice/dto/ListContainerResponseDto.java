package by.arvisit.cabapp.ridesservice.dto;

import java.util.List;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ListContainerResponseDto<T>(
        List<T> values) {

}
