package by.arvisit.cabapp.paymentservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.paymentservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;

public interface PassengerPaymentService {

    PassengerPaymentResponseDto save(PassengerPaymentRequestDto dto);

    PassengerPaymentResponseDto getPaymentById(String id);

    ListContainerResponseDto<PassengerPaymentResponseDto> getPayments(Pageable pageable);

}
