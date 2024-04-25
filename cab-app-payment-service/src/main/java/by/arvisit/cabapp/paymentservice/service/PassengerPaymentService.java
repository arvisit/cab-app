package by.arvisit.cabapp.paymentservice.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;

public interface PassengerPaymentService {

    PassengerPaymentResponseDto save(PassengerPaymentRequestDto dto);

    PassengerPaymentResponseDto getPaymentById(String id);

    ListContainerResponseDto<PassengerPaymentResponseDto> getPayments(Pageable pageable, Map<String, String> params);

}
