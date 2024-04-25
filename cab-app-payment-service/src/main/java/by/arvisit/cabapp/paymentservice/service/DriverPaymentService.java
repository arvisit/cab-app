package by.arvisit.cabapp.paymentservice.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;

public interface DriverPaymentService {

    DriverPaymentResponseDto save(DriverPaymentRequestDto dto);

    DriverPaymentResponseDto getPaymentById(String id);

    ListContainerResponseDto<DriverPaymentResponseDto> getPayments(Pageable pageable, Map<String, String> params);

    DriverAccountBalanceResponseDto getDriverAccountBalance(String id);
}
