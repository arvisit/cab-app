package by.arvisit.cabapp.paymentservice.service.impl;

import static by.arvisit.cabapp.paymentservice.util.PaginationUtil.getLastPageNumber;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.mapper.DriverPaymentMapper;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import by.arvisit.cabapp.paymentservice.service.DriverPaymentService;
import by.arvisit.cabapp.paymentservice.util.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverPaymentServiceImpl implements DriverPaymentService {

    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment.id.EntityNotFoundException.template";

    private final DriverPaymentRepository driverPaymentRepository;
    private final DriverPaymentMapper driverPaymentMapper;
    private final MessageSource messageSource;
    private final DriverPaymentVerifier driverPaymentVerifier;

    @Transactional
    @Override
    public DriverPaymentResponseDto save(DriverPaymentRequestDto dto) {
        log.debug("Call for DriverPaymentService.save() with dto: {}", dto);

        DriverPayment newPayment = driverPaymentMapper.fromRequestDtoToEntity(dto);

        driverPaymentVerifier.verifyNewPayment(newPayment);

        newPayment.setStatus(PaymentStatusEnum.SUCCESS);
        newPayment.setTimestamp(ZonedDateTime.now(AppConstants.EUROPE_MINSK_TIMEZONE));

        return driverPaymentMapper.fromEntityToResponseDto(
                driverPaymentRepository.save(newPayment));
    }

    @Transactional(readOnly = true)
    @Override
    public DriverPaymentResponseDto getPaymentById(String id) {
        log.debug("Call for DriverPaymentService.getPaymentById() with id {}", id);

        return driverPaymentMapper.fromEntityToResponseDto(
                findPaymentByIdOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<DriverPaymentResponseDto> getPayments(Pageable pageable) {
        log.debug("Call for DriverPaymentService.getPayments() with pageable settings: {}", pageable);

        List<DriverPaymentResponseDto> payments = driverPaymentRepository.findAll(pageable).stream()
                .map(driverPaymentMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<DriverPaymentResponseDto>builder()
                .withValues(payments)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(driverPaymentRepository.count(), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public DriverAccountBalanceResponseDto getDriverAccountBalance(String id) {
        log.debug("Call for DriverPaymentService.getDriverAccountBalance() with id {}", id);

        BigDecimal balance = driverPaymentRepository.getDriverAccountBalance(UUID.fromString(id));

        return DriverAccountBalanceResponseDto.builder()
                .withDriverId(id)
                .withBalance(balance)
                .build();
    }

    private DriverPayment findPaymentByIdOrThrowException(String id) {
        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);

        return driverPaymentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));
    }

}
