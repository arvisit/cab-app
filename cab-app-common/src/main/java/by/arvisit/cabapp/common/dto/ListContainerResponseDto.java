package by.arvisit.cabapp.common.dto;

import java.util.List;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ListContainerResponseDto<T>(
        Integer currentPage,
        Integer size,
        Integer lastPage,
        String sort,
        List<T> values) {

}
