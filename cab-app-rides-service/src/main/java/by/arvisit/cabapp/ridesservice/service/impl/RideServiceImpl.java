package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.ridesservice.util.PaginationUtil.getLastPageNumber;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.exceptionhandlingstarter.exception.ValueAlreadyInUseException;
import by.arvisit.cabapp.ridesservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.mapper.RideMapper;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import by.arvisit.cabapp.ridesservice.service.CostService;
import by.arvisit.cabapp.ridesservice.service.PromoCodeService;
import by.arvisit.cabapp.ridesservice.service.RideService;
import by.arvisit.cabapp.ridesservice.util.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideServiceImpl implements RideService {

    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.id.EntityNotFoundException.template";
    private static final String STATUS_COULD_NOT_BE_CHANGED_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.status.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_FOR_PAYMENT_CONFIRMATION_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.paid.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_APPLY_PROMO_CODE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.promoCode.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_CHANGE_PAYMENT_METHOD_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.paymentMethod.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_SCORE_DRIVER_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.driverScore.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_SCORE_PASSENGER_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.passengerScore.IllegalStateException.template";
    private static final String RIDE_ALREADY_ACCEPTED_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.driverId.IllegalStateException.template";
    private static final String INVALID_PAYMENT_METHOD_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.dto.RideRequestDto.paymentMethod.isValidPaymentMethod.message";

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final MessageSource messageSource;
    private final CostService costService;
    private final PromoCodeService promoCodeService;

    @Transactional
    @Override
    public RideResponseDto save(RideRequestDto dto) {
        log.debug("Call for RideService.save() with dto {}", dto);

        Ride rideToSave = rideMapper.fromRequestDtoToEntity(dto);

        BigDecimal initialCost = costService.calculateInitialRideCost(
                rideToSave.getStartAddress(),
                rideToSave.getDestinationAddress());

        rideToSave.setInitialCost(initialCost);
        rideToSave.setFinalCost(initialCost);
        rideToSave.setBookRide(ZonedDateTime.now(AppConstants.EUROPE_MINSK_TIMEZONE));
        rideToSave.setIsPaid(false);
        rideToSave.setStatus(RideStatusEnum.BOOKED);

        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(rideToSave));
    }

    @Transactional
    @Override
    public RideResponseDto cancelRide(String id) {
        log.debug("Call for RideService.cancelRide() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();

        RideStatusEnum newStatus = RideStatusEnum.CANCELED;

        if (currentStatus == RideStatusEnum.CANCELED) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        if (currentStatus == RideStatusEnum.BEGIN_RIDE || currentStatus == RideStatusEnum.END_RIDE
                || currentStatus == RideStatusEnum.FINISHED) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }

        ride.setStatus(newStatus);
        ride.setCancelRide(ZonedDateTime.now(AppConstants.EUROPE_MINSK_TIMEZONE));
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto acceptRide(String rideId, String driverId) {
        log.debug("Call for RideService.acceptRide() with rideId {} by driver with id {}", rideId, driverId);

        Ride ride = findRideByIdOrThrowException(rideId);
        RideStatusEnum currentStatus = ride.getStatus();

        RideStatusEnum newStatus = RideStatusEnum.ACCEPTED;
        if (currentStatus == RideStatusEnum.CANCELED || currentStatus == RideStatusEnum.BEGIN_RIDE
                || currentStatus == RideStatusEnum.END_RIDE || currentStatus == RideStatusEnum.FINISHED) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }

        if (currentStatus == RideStatusEnum.ACCEPTED && driverId.equals(ride.getDriverId().toString())) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        if (currentStatus == RideStatusEnum.ACCEPTED && !driverId.equals(ride.getDriverId().toString())) {
            String errorMessage = messageSource.getMessage(
                    RIDE_ALREADY_ACCEPTED_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId(), driverId }, null);
            throw new IllegalStateException(errorMessage);
        }

        ride.setStatus(newStatus);
        ride.setDriverId(UUID.fromString(driverId));
        ride.setAcceptRide(ZonedDateTime.now(AppConstants.EUROPE_MINSK_TIMEZONE));
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto beginRide(String id) {
        log.debug("Call for RideService.beginRide() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();

        RideStatusEnum newStatus = RideStatusEnum.BEGIN_RIDE;
        if (currentStatus != RideStatusEnum.ACCEPTED && currentStatus != RideStatusEnum.BEGIN_RIDE) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }

        if (currentStatus == RideStatusEnum.BEGIN_RIDE) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setBeginRide(ZonedDateTime.now(AppConstants.EUROPE_MINSK_TIMEZONE));
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto endRide(String id) {
        log.debug("Call for RideService.endRide() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();

        RideStatusEnum newStatus = RideStatusEnum.END_RIDE;
        if (currentStatus != RideStatusEnum.BEGIN_RIDE && currentStatus != RideStatusEnum.END_RIDE) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }

        if (currentStatus == RideStatusEnum.END_RIDE) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setEndRide(ZonedDateTime.now(AppConstants.EUROPE_MINSK_TIMEZONE));
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto finishRide(String id) {
        log.debug("Call for RideService.finishRide() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();

        RideStatusEnum newStatus = RideStatusEnum.FINISHED;
        if (currentStatus != RideStatusEnum.END_RIDE && currentStatus != RideStatusEnum.FINISHED && !ride.getIsPaid()) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }

        if (currentStatus == RideStatusEnum.FINISHED) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setFinishRide(ZonedDateTime.now(AppConstants.EUROPE_MINSK_TIMEZONE));
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto confirmPayment(String id) {
        log.debug("Call for RideService.confirmPayment() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus != RideStatusEnum.END_RIDE && currentStatus != RideStatusEnum.FINISHED) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_FOR_PAYMENT_CONFIRMATION_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }

        if (currentStatus == RideStatusEnum.FINISHED || ride.getIsPaid()) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setIsPaid(true);
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto applyPromoCode(String rideId, String promoCodeKeyword) {
        log.debug("Call for RideService.applyPromoCode() with rideId {} and promo code keyword '{}'", rideId,
                promoCodeKeyword);

        Ride ride = findRideByIdOrThrowException(rideId);
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus == RideStatusEnum.CANCELED || currentStatus == RideStatusEnum.FINISHED || ride.getIsPaid()) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_APPLY_PROMO_CODE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }

        PromoCodeResponseDto promoCodeDto = promoCodeService.getActivePromoCodeByKeyword(promoCodeKeyword);
        PromoCode promoCodeEntity = PromoCode.builder()
                .withId(promoCodeDto.id())
                .withKeyword(promoCodeDto.keyword())
                .withDiscountPercent(promoCodeDto.discountPercent())
                .withIsActive(promoCodeDto.isActive())
                .build();

        BigDecimal finalCost = costService.calculateRideCostWithPromoCode(ride.getInitialCost(), promoCodeEntity);
        ride.setFinalCost(finalCost);
        ride.setPromoCode(promoCodeEntity);

        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto changePaymentMethod(String rideId, String paymentMethod) {
        log.debug("Call for RideService.changePaymentMethod() with rideId {} and payment method {}", rideId,
                paymentMethod);

        Ride ride = findRideByIdOrThrowException(rideId);
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus == RideStatusEnum.CANCELED || currentStatus == RideStatusEnum.FINISHED || ride.getIsPaid()) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_CHANGE_PAYMENT_METHOD_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }

        PaymentMethodEnum newPaymentMethod;
        try {
            newPaymentMethod = PaymentMethodEnum.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            String errorMessage = messageSource.getMessage(
                    INVALID_PAYMENT_METHOD_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalArgumentException(errorMessage);
        }
        ride.setPaymentMethod(newPaymentMethod);
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto scoreDriver(String rideId, Integer score) {
        log.debug("Call for RideService.scoreDriver() with rideId {} and score {}", rideId, score);

        Ride ride = findRideByIdOrThrowException(rideId);
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus != RideStatusEnum.FINISHED || ride.getDriverScore() != null) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_SCORE_DRIVER_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }

        ride.setDriverScore(score);
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto scorePassenger(String rideId, Integer score) {
        log.debug("Call for RideService.scorePassenger() with rideId {} and score {}", rideId, score);

        Ride ride = findRideByIdOrThrowException(rideId);
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus != RideStatusEnum.FINISHED || ride.getPassengerScore() != null) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_SCORE_PASSENGER_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }

        ride.setPassengerScore(score);
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public void delete(String id) {
        log.debug("Call for RideService.delete() with id {}", id);

        UUID uuid = UUID.fromString(id);
        if (!rideRepository.existsById(uuid)) {
            String errorMessage = messageSource.getMessage(
                    FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                    new Object[] { id }, null);
            throw new ValueAlreadyInUseException(errorMessage);
        }
        rideRepository.deleteById(uuid);
    }

    @Transactional(readOnly = true)
    @Override
    public RideResponseDto getRideById(String id) {
        log.debug("Call for RideService.getRideById() with id {}", id);

        return rideMapper.fromEntityToResponseDto(
                findRideByIdOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<RideResponseDto> getRides(Pageable pageable) {
        log.debug("Call for RideService.getRides() with pageable settings: {}", pageable);

        List<RideResponseDto> rides = rideRepository.findAll(pageable).stream()
                .map(rideMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<RideResponseDto>builder()
                .withValues(rides)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(rideRepository.count(), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<RideResponseDto> getRidesByPassengerId(String id, Pageable pageable) {
        log.debug("Call for RideService.getRidesByPassengerId() with id {} and pageable settings: {}", id, pageable);

        UUID uuid = UUID.fromString(id);
        List<RideResponseDto> rides = rideRepository.findByPassengerId(uuid, pageable).stream()
                .map(rideMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<RideResponseDto>builder()
                .withValues(rides)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(rideRepository.countByPassengerId(uuid), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<RideResponseDto> getRidesByDriverId(String id, Pageable pageable) {
        log.debug("Call for RideService.getRidesByDriverId() with id {} and pageable settings: {}", id, pageable);

        UUID uuid = UUID.fromString(id);
        List<RideResponseDto> rides = rideRepository.findByDriverId(uuid, pageable).stream()
                .map(rideMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<RideResponseDto>builder()
                .withValues(rides)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(rideRepository.countByDriverId(uuid), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    private Ride findRideByIdOrThrowException(String id) {
        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        return rideRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new EntityNotFoundException(errorMessage));
    }

    private void throwExceptionForIllegalStatusChange(Ride ride, RideStatusEnum newStatus) {
        String errorMessage = messageSource.getMessage(
                STATUS_COULD_NOT_BE_CHANGED_MESSAGE_TEMPLATE_KEY,
                new Object[] { ride.getId(), ride.getStatus(), newStatus }, null);
        throw new IllegalStateException(errorMessage);
    }
}
