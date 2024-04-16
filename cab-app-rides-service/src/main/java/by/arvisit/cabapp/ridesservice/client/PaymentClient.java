package by.arvisit.cabapp.ridesservice.client;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;

public interface PaymentClient {

    PassengerPaymentResponseDto save(PassengerPaymentRequestDto dto);
}
