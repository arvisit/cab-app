package by.arvisit.cabapp.paymentservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.dto.ListContainerResponseDto;

public interface DriverPaymentService {

    DriverPaymentResponseDto save(DriverPaymentRequestDto dto);

    DriverPaymentResponseDto getPaymentById(String id);

    ListContainerResponseDto<DriverPaymentResponseDto> getPayments(Pageable pageable);

    DriverAccountBalanceResponseDto getDriverAccountBalance(String id);
}
