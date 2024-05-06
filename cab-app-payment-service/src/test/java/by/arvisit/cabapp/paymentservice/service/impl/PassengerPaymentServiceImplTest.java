package by.arvisit.cabapp.paymentservice.service.impl;

import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_PAYMENT_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getListContainerForResponse;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPayment;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPaymentResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.mapper.PassengerPaymentMapper;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import by.arvisit.cabapp.paymentservice.persistence.repository.PassengerPaymentRepository;
import by.arvisit.cabapp.paymentservice.persistence.util.PassengerPaymentSpecs;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PassengerPaymentServiceImplTest {

    @InjectMocks
    private PassengerPaymentServiceImpl passengerPaymentService;
    @Mock
    private PassengerPaymentRepository passengerPaymentRepository;
    @Mock
    private PassengerPaymentMapper passengerPaymentMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private PassengerPaymentVerifier passengerPaymentVerifier;
    @Mock
    private StreamBridge streamBridge;
    @Mock
    private PassengerPaymentSpecs passengerPaymentSpecs;

    @Test
    void shouldReturnPassengerResponseDto_whenSaveWithValidInput() {
        PassengerPaymentResponseDto responseDto = getPassengerPaymentResponseDto().build();
        PassengerPayment payment = getPassengerPayment().build();

        when(passengerPaymentMapper.fromRequestDtoToEntity(any(PassengerPaymentRequestDto.class)))
                .thenReturn(payment);
        doNothing().when(passengerPaymentVerifier)
                .verifyNewPayment(any(PassengerPayment.class));
        when(passengerPaymentRepository.save(any(PassengerPayment.class)))
                .thenReturn(payment);
        when(passengerPaymentMapper.fromEntityToResponseDto(any(PassengerPayment.class)))
                .thenReturn(responseDto);

        PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto().build();
        PassengerPaymentResponseDto result = passengerPaymentService.save(requestDto);

        assertThat(result)
                .isEqualTo(responseDto);
    }

    @Test
    void shouldReturnPassengerResponseDto_whenGetPaymentByIdForExistingId() {
        PassengerPaymentResponseDto responseDto = getPassengerPaymentResponseDto().build();
        PassengerPayment payment = getPassengerPayment().build();

        when(passengerPaymentRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(payment));
        when(passengerPaymentMapper.fromEntityToResponseDto(any(PassengerPayment.class)))
                .thenReturn(responseDto);

        PassengerPaymentResponseDto result = passengerPaymentService.getPaymentById(PASSENGER_PAYMENT_ID_STRING);

        assertThat(result)
                .isEqualTo(responseDto);

    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetPaymentByIdForNonExistingId() {
        when(passengerPaymentRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerPaymentService.getPaymentById(PASSENGER_PAYMENT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnContainerWithPassengerPaymentResponseDto_whenGetPayments() {

        PassengerPaymentResponseDto passengerPaymentResponseDto = getPassengerPaymentResponseDto()
                .build();
        PassengerPayment passengerPayment = getPassengerPayment().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<PassengerPayment> passengerPayments = List.of(passengerPayment);
        Page<PassengerPayment> passengerPaymentsPage = new PageImpl<>(passengerPayments);
        Specification<PassengerPayment> spec = (root, query, cb) -> cb.conjunction();

        when(passengerPaymentSpecs.getAllByFilter(any()))
                .thenReturn(spec);
        when(passengerPaymentRepository.findAll(spec, pageable))
                .thenReturn(passengerPaymentsPage);
        when(passengerPaymentMapper.fromEntityToResponseDto(any(PassengerPayment.class)))
                .thenReturn(passengerPaymentResponseDto);
        when(passengerPaymentRepository.count(spec))
                .thenReturn((long) passengerPayments.size());

        ListContainerResponseDto<PassengerPaymentResponseDto> result = passengerPaymentService.getPayments(pageable,
                Collections.emptyMap());
        ListContainerResponseDto<PassengerPaymentResponseDto> expected = getListContainerForResponse(
                PassengerPaymentResponseDto.class)
                .withValues(List.of(passengerPaymentResponseDto))
                .build();

        assertThat(result)
                .isEqualTo(expected);
    }
}
