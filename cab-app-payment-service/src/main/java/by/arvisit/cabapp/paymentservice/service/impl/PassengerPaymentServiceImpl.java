package by.arvisit.cabapp.paymentservice.service.impl;

import static by.arvisit.cabapp.common.util.PaginationUtil.getLastPageNumber;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.util.CommonConstants;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentsFilterParams;
import by.arvisit.cabapp.paymentservice.mapper.PassengerPaymentMapper;
import by.arvisit.cabapp.paymentservice.mapper.PassengerPaymentsFilterParamsMapper;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.PassengerPaymentRepository;
import by.arvisit.cabapp.paymentservice.persistence.util.PassengerPaymentSpecs;
import by.arvisit.cabapp.paymentservice.service.PassengerPaymentService;
import by.arvisit.cabapp.paymentservice.util.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerPaymentServiceImpl implements PassengerPaymentService {

    private static final String OUT_CARD_PAYMENT_CREATED_CHANNEL = "outCardPaymentCreated";
    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment.id.EntityNotFoundException.template";

    private final PassengerPaymentRepository passengerPaymentRepository;
    private final PassengerPaymentMapper passengerPaymentMapper;
    private final PassengerPaymentsFilterParamsMapper filterParamsMapper;
    private final MessageSource messageSource;
    private final PassengerPaymentVerifier passengerPaymentVerifier;
    private final StreamBridge streamBridge;
    private final PassengerPaymentSpecs passengerPaymentSpecs;

    @Transactional
    @Override
    public PassengerPaymentResponseDto save(PassengerPaymentRequestDto dto) {
        log.debug("Call for PassengerPaymentService.save() with dto: {}", dto);

        PassengerPayment newPayment = passengerPaymentMapper.fromRequestDtoToEntity(dto);

        passengerPaymentVerifier.verifyNewPayment(newPayment);

        BigDecimal aggregatorFeeAmount = dto.amount().multiply(AppConstants.AGGREGATOR_FEE_PERCENT)
                .divide(AppConstants.BIG_DECIMAL_HUNDRED);

        newPayment.setFeeAmount(aggregatorFeeAmount);
        newPayment.setTimestamp(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));
        newPayment.setStatus(PaymentStatusEnum.SUCCESS);

        PassengerPaymentResponseDto paymentToSend = passengerPaymentMapper.fromEntityToResponseDto(
                passengerPaymentRepository.save(newPayment));

        if (newPayment.getPaymentMethod() == PaymentMethodEnum.BANK_CARD) {
            Message<PassengerPaymentResponseDto> message = MessageBuilder.withPayload(paymentToSend).build();
            streamBridge.send(OUT_CARD_PAYMENT_CREATED_CHANNEL, message);
        }

        return paymentToSend;
    }

    @Transactional(readOnly = true)
    @Override
    public PassengerPaymentResponseDto getPaymentById(String id) {
        log.debug("Call for PassengerPaymentService.getPaymentById() with id {}", id);

        return passengerPaymentMapper.fromEntityToResponseDto(
                findPaymentByIdOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<PassengerPaymentResponseDto> getPayments(Pageable pageable,
            Map<String, String> params) {
        log.debug(
                "Call for PassengerPaymentService.getPayments() with pageable settings: {} and request parameters: {}",
                pageable, params);

        PassengerPaymentsFilterParams filterParams = filterParamsMapper.fromMapParams(params);
        Specification<PassengerPayment> spec = passengerPaymentSpecs.getAllByFilter(filterParams);
        List<PassengerPaymentResponseDto> payments = passengerPaymentRepository.findAll(spec, pageable).stream()
                .map(passengerPaymentMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<PassengerPaymentResponseDto>builder()
                .withValues(payments)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(passengerPaymentRepository.count(spec), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    private PassengerPayment findPaymentByIdOrThrowException(String id) {
        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);

        return passengerPaymentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));
    }

}
