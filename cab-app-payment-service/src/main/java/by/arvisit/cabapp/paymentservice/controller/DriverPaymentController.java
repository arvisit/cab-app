package by.arvisit.cabapp.paymentservice.controller;

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
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.service.DriverPaymentService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/driver-payments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DriverPaymentController {

    private final DriverPaymentService driverPaymentService;

    @PostMapping
    public ResponseEntity<DriverPaymentResponseDto> save(@RequestBody @Valid DriverPaymentRequestDto dto) {
        DriverPaymentResponseDto response = driverPaymentService.save(dto);

        log.debug("New driver payment was added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public DriverPaymentResponseDto getPaymentById(@PathVariable @UUID String id) {
        DriverPaymentResponseDto response = driverPaymentService.getPaymentById(id);

        log.debug("Got driver payment with id {}: {}", id, response);
        return response;
    }

    @GetMapping
    public ListContainerResponseDto<DriverPaymentResponseDto> getPayments(
            @PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<DriverPaymentResponseDto> response = driverPaymentService.getPayments(pageable);

        log.debug("Got all driver payments. Total count: {}. Pageable settings: {}", response.values().size(),
                pageable);
        return response;
    }

    @GetMapping("/drivers/{id}/balance")
    public DriverAccountBalanceResponseDto getDriverAccountBalance(@PathVariable @UUID String id) {
        DriverAccountBalanceResponseDto response = driverPaymentService.getDriverAccountBalance(id);

        log.debug("Got account balance for driver with id {}: {}", id, response.balance());
        return response;
    }
}
