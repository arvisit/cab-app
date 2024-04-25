package by.arvisit.cabapp.paymentservice.controller;

import java.util.Map;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.validation.MapContainsAllowedKeys;
import by.arvisit.cabapp.common.validation.MapContainsParseableDateValues;
import by.arvisit.cabapp.common.validation.MapContainsParseableUUIDValues;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.service.PassengerPaymentService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/passenger-payments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PassengerPaymentController {

    private static final String REQUEST_PARAMS_VALIDATION_NOT_ALLOWED_KEYS_MESSAGE = "{by.arvisit.cabapp.paymentservice.controller.PassengerPaymentController.requestParams.MapContainsAllowedKeys.message}";
    private final PassengerPaymentService passengerPaymentService;

    @PostMapping
    public ResponseEntity<PassengerPaymentResponseDto> save(@RequestBody @Valid PassengerPaymentRequestDto dto) {
        PassengerPaymentResponseDto response = passengerPaymentService.save(dto);

        log.debug("New passenger payment was added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public PassengerPaymentResponseDto getPaymentById(@PathVariable @UUID String id) {
        PassengerPaymentResponseDto response = passengerPaymentService.getPaymentById(id);

        log.debug("Got passenger payment with id {}: {}", id, response);
        return response;
    }

    @GetMapping
    public ListContainerResponseDto<PassengerPaymentResponseDto> getPayments(
            @PageableDefault @Nullable @Valid Pageable pageable,
            @RequestParam @Nullable
            @MapContainsAllowedKeys(
                    keys = { "page", "size", "sort", "status", "paymentMethod", "passengerId", "driverId",
                            "timestamp" },
                    message = REQUEST_PARAMS_VALIDATION_NOT_ALLOWED_KEYS_MESSAGE)
            @MapContainsParseableUUIDValues(
                    keys = { "passengerId", "driverId" })
            @MapContainsParseableDateValues(
                    keys = { "timestamp" }) Map<String, @NotBlank String> requestParams) {

        log.debug("Get all passenger payments according to request parameters: {}", requestParams);
        ListContainerResponseDto<PassengerPaymentResponseDto> response = passengerPaymentService.getPayments(pageable,
                requestParams);

        log.debug("Got all passenger payments. Total count: {}. Pageable settings: {}", response.values().size(),
                pageable);
        return response;
    }
}
