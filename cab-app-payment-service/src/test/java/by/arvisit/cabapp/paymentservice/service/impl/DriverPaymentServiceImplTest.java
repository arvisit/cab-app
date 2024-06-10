package by.arvisit.cabapp.paymentservice.service.impl;

import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DRIVER_ACCOUNT_BALANCE_ENOUGH;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DRIVER_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_PAYMENT_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverAccountBalanceResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPayment;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getEmptyDriverPaymentsFilterParams;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getListContainerForResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.mapper.DriverPaymentMapper;
import by.arvisit.cabapp.paymentservice.mapper.DriverPaymentsFilterParamsMapper;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import by.arvisit.cabapp.paymentservice.persistence.util.DriverPaymentSpecs;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DriverPaymentServiceImplTest {

    @InjectMocks
    private DriverPaymentServiceImpl driverPaymentService;
    @Mock
    private DriverPaymentRepository driverPaymentRepository;
    @Mock
    private DriverPaymentMapper driverPaymentMapper;
    @Mock
    private DriverPaymentsFilterParamsMapper filterParamsMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private DriverPaymentVerifier driverPaymentVerifier;
    @Mock
    private DriverPaymentSpecs driverPaymentSpecs;

    @Test
    void shouldReturnDriverResponseDto_whenSaveWithValidInput() {
        DriverPaymentResponseDto responseDto = getDriverPaymentResponseDto().build();
        DriverPayment payment = getDriverPayment().build();

        when(driverPaymentMapper.fromRequestDtoToEntity(any(DriverPaymentRequestDto.class)))
                .thenReturn(payment);
        doNothing().when(driverPaymentVerifier)
                .verifyNewPayment(any(DriverPayment.class));
        when(driverPaymentRepository.save(any(DriverPayment.class)))
                .thenReturn(payment);
        when(driverPaymentMapper.fromEntityToResponseDto(any(DriverPayment.class)))
                .thenReturn(responseDto);

        DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto().build();
        DriverPaymentResponseDto result = driverPaymentService.save(requestDto);

        assertThat(result)
                .isEqualTo(responseDto);
    }

    @Test
    void shouldReturnDriverResponseDto_whenGetPaymentByIdForExistingId() {
        DriverPaymentResponseDto responseDto = getDriverPaymentResponseDto().build();
        DriverPayment payment = getDriverPayment().build();

        when(driverPaymentRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(payment));
        when(driverPaymentMapper.fromEntityToResponseDto(any(DriverPayment.class)))
                .thenReturn(responseDto);

        DriverPaymentResponseDto result = driverPaymentService.getPaymentById(PASSENGER_PAYMENT_ID_STRING);

        assertThat(result)
                .isEqualTo(responseDto);

    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetPaymentByIdForNonExistingId() {
        when(driverPaymentRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverPaymentService.getPaymentById(PASSENGER_PAYMENT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnContainerWithDriverPaymentResponseDto_whenGetPayments() {

        DriverPaymentResponseDto driverPaymentResponseDto = getDriverPaymentResponseDto()
                .build();
        DriverPayment driverPayment = getDriverPayment().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<DriverPayment> driverPayments = List.of(driverPayment);
        Page<DriverPayment> driverPaymentsPage = new PageImpl<>(driverPayments);
        Specification<DriverPayment> spec = (root, query, cb) -> cb.conjunction();

        when(filterParamsMapper.fromMapParams(any()))
                .thenReturn(getEmptyDriverPaymentsFilterParams().build());
        when(driverPaymentSpecs.getAllByFilter(any()))
                .thenReturn(spec);
        when(driverPaymentRepository.findAll(spec, pageable))
                .thenReturn(driverPaymentsPage);
        when(driverPaymentMapper.fromEntityToResponseDto(any(DriverPayment.class)))
                .thenReturn(driverPaymentResponseDto);
        when(driverPaymentRepository.count(spec))
                .thenReturn((long) driverPayments.size());

        ListContainerResponseDto<DriverPaymentResponseDto> result = driverPaymentService.getPayments(pageable,
                Collections.emptyMap());
        ListContainerResponseDto<DriverPaymentResponseDto> expected = getListContainerForResponse(
                DriverPaymentResponseDto.class)
                .withValues(List.of(driverPaymentResponseDto))
                .build();

        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void shouldReturnDriverAccountBalanceResponseDto_whenGetDriverAccountBalance() {
        BigDecimal balance = DRIVER_ACCOUNT_BALANCE_ENOUGH;

        when(driverPaymentRepository.getDriverAccountBalance(any(UUID.class)))
                .thenReturn(balance);

        DriverAccountBalanceResponseDto expected = getDriverAccountBalanceResponseDto().build();
        DriverAccountBalanceResponseDto result = driverPaymentService.getDriverAccountBalance(DRIVER_ID_STRING);

        assertThat(result)
                .isEqualTo(expected);
    }
}
