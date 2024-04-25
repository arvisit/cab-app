package by.arvisit.cabapp.paymentservice.service.impl;

import static by.arvisit.cabapp.common.util.PaginationUtil.getLastPageNumber;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.util.CommonConstants;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.mapper.DriverPaymentMapper;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import by.arvisit.cabapp.paymentservice.persistence.util.DriverPaymentSpecs;
import by.arvisit.cabapp.paymentservice.service.DriverPaymentService;
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
    private final DriverPaymentSpecs driverPaymentSpecs;

    @Transactional
    @Override
    public DriverPaymentResponseDto save(DriverPaymentRequestDto dto) {
        log.debug("Call for DriverPaymentService.save() with dto: {}", dto);

        DriverPayment newPayment = driverPaymentMapper.fromRequestDtoToEntity(dto);

        driverPaymentVerifier.verifyNewPayment(newPayment);

        newPayment.setStatus(PaymentStatusEnum.SUCCESS);
        newPayment.setTimestamp(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));

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
    public ListContainerResponseDto<DriverPaymentResponseDto> getPayments(Pageable pageable,
            Map<String, String> params) {
        log.debug("Call for DriverPaymentService.getPayments() with pageable settings: {} and request parameters: {}",
                pageable, params);

        Specification<DriverPayment> spec = driverPaymentSpecs.getAllByFilter(params);
        List<DriverPaymentResponseDto> payments = driverPaymentRepository.findAll(spec, pageable).stream()
                .map(driverPaymentMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<DriverPaymentResponseDto>builder()
                .withValues(payments)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(driverPaymentRepository.count(spec), pageable.getPageSize()))
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
