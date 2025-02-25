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
import by.arvisit.cabapp.common.validation.AllowedKeys;
import by.arvisit.cabapp.common.validation.ParseableDateValues;
import by.arvisit.cabapp.common.validation.ParseableUUIDValues;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentsFilterParams;
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
            @AllowedKeys(keysHolder = PassengerPaymentsFilterParams.class)
            @ParseableUUIDValues(keysHolder = PassengerPaymentsFilterParams.class)
            @ParseableDateValues(
                    keysHolder = PassengerPaymentsFilterParams.class) Map<String, @NotBlank String> requestParams) {

        log.debug("Get all passenger payments according to request parameters: {}", requestParams);
        ListContainerResponseDto<PassengerPaymentResponseDto> response = passengerPaymentService.getPayments(pageable,
                requestParams);

        log.debug("Got all passenger payments. Total count: {}. Pageable settings: {}", response.values().size(),
                pageable);
        return response;
    }
}
