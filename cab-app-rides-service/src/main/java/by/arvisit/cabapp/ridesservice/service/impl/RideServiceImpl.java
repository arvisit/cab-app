package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.common.util.PaginationUtil.getLastPageNumber;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.common.util.CommonConstants;
import by.arvisit.cabapp.ridesservice.client.DriverClient;
import by.arvisit.cabapp.ridesservice.client.PassengerClient;
import by.arvisit.cabapp.ridesservice.client.PaymentClient;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RidesFilterParams;
import by.arvisit.cabapp.ridesservice.mapper.RideMapper;
import by.arvisit.cabapp.ridesservice.mapper.RidesFilterParamsMapper;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import by.arvisit.cabapp.ridesservice.persistence.util.RideSpecs;
import by.arvisit.cabapp.ridesservice.service.CostService;
import by.arvisit.cabapp.ridesservice.service.PromoCodeService;
import by.arvisit.cabapp.ridesservice.service.RideService;
import by.arvisit.cabapp.ridesservice.util.PaymentVerifier;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideServiceImpl implements RideService {

    private static final String OUT_FINISH_RIDE_CHANNEL = "outFinishRide";
    private static final Map<String, Boolean> SET_NOT_AVAILABLE_PATCH = Collections.singletonMap("isAvailable", false);
    private static final String OUT_CANCELED_RIDE_CHANNEL = "outCanceledRide";
    private static final String OUT_ACCEPTED_RIDE_CHANNEL = "outAcceptedRide";
    private static final String OUT_NEW_RIDE_CHANNEL = "outNewRide";
    private static final String OUT_CREATE_CARD_PAYMENT_CHANNEL = "outCreateCardPayment";
    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.id.EntityNotFoundException.template";
    private static final String INVALID_PAYMENT_METHOD_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.dto.RideRequestDto.paymentMethod.isValidPaymentMethod.message";

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final RidesFilterParamsMapper filterParamsMapper;
    private final MessageSource messageSource;
    private final CostService costService;
    private final PromoCodeService promoCodeService;
    private final RideVerifier rideVerifier;
    private final RideSecurityVerifier rideSecurityVerifier;
    private final PaymentClient paymentClient;
    private final PassengerClient passengerClient;
    private final StreamBridge streamBridge;
    private final PaymentVerifier paymentVerifier;
    private final DriverClient driverClient;
    private final RideSpecs rideSpecs;

    @Transactional
    @Override
    public RideResponseDto save(RideRequestDto dto) {
        log.debug("Call for RideService.save() with dto {}", dto);

        Ride rideToSave = rideMapper.fromRequestDtoToEntity(dto);

        rideVerifier.verifyCreateRide(rideToSave);

        BigDecimal initialCost = costService.calculateInitialRideCost(
                rideToSave.getStartAddress(),
                rideToSave.getDestinationAddress());

        rideToSave.setInitialCost(initialCost);
        rideToSave.setFinalCost(initialCost);
        rideToSave.setBookRide(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));
        rideToSave.setIsPaid(false);
        rideToSave.setStatus(RideStatusEnum.BOOKED);

        RideResponseDto savedRide = rideMapper.fromEntityToResponseDto(
                rideRepository.save(rideToSave));

        Message<RideResponseDto> message = MessageBuilder.withPayload(savedRide).build();
        streamBridge.send(OUT_NEW_RIDE_CHANNEL, message);

        return savedRide;
    }

    @Transactional
    @Override
    public RideResponseDto cancelRide(String id) {
        log.debug("Call for RideService.cancelRide() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.CANCELED;

        rideVerifier.verifyCancelRide(ride);
        rideSecurityVerifier.verifyAllowedForTheRidePassenger(ride);

        if (currentStatus == newStatus) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setCancelRide(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));

        RideResponseDto savedRide = rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));

        Message<RideResponseDto> message = MessageBuilder.withPayload(savedRide).build();
        streamBridge.send(OUT_CANCELED_RIDE_CHANNEL, message);

        return savedRide;
    }

    @Transactional
    @Override
    public RideResponseDto acceptRide(String rideId, String driverId) {
        log.debug("Call for RideService.acceptRide() with rideId {} by driver with id {}", rideId, driverId);

        Ride ride = findRideByIdOrThrowException(rideId);
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.ACCEPTED;

        rideVerifier.verifyAcceptRide(ride, driverId);

        if (currentStatus == newStatus && driverId.equals(ride.getDriverId().toString())) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setDriverId(UUID.fromString(driverId));
        ride.setAcceptRide(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));

        RideResponseDto savedRide = rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));

        driverClient.updateAvailability(driverId, SET_NOT_AVAILABLE_PATCH);

        Message<RideResponseDto> message = MessageBuilder.withPayload(savedRide).build();
        streamBridge.send(OUT_ACCEPTED_RIDE_CHANNEL, message);

        return savedRide;
    }

    @Transactional
    @Override
    public RideResponseDto beginRide(String id) {
        log.debug("Call for RideService.beginRide() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.BEGIN_RIDE;

        rideVerifier.verifyBeginRide(ride);
        rideSecurityVerifier.verifyAllowedForTheRideDriver(ride);

        if (currentStatus == newStatus) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setBeginRide(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));
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

        rideVerifier.verifyEndRide(ride);
        rideSecurityVerifier.verifyAllowedForTheRideDriver(ride);

        if (currentStatus == newStatus) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setEndRide(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));

        if (ride.getPaymentMethod() == PaymentMethodEnum.BANK_CARD) {
            PassengerResponseDto passenger = passengerClient.getPassengerById(ride.getPassengerId().toString());

            PassengerPaymentRequestDto payment = rideMapper.fromRideToPassengerPaymentRequestDto(ride,
                    passenger.cardNumber());

            Message<PassengerPaymentRequestDto> message = MessageBuilder.withPayload(payment).build();
            streamBridge.send(OUT_CREATE_CARD_PAYMENT_CHANNEL, message);
        }

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

        rideVerifier.verifyFinishRide(ride);
        rideSecurityVerifier.verifyAllowedForTheRideDriver(ride);

        if (currentStatus == newStatus) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        ride.setStatus(newStatus);
        ride.setFinishRide(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto confirmPayment(String id) {
        log.debug("Call for RideService.confirmPayment() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);
        RideStatusEnum currentStatus = ride.getStatus();

        rideVerifier.verifyConfirmPayment(ride);
        rideSecurityVerifier.verifyAllowedForTheRideDriverOrMessageBroker(ride);

        if (currentStatus == RideStatusEnum.FINISHED || ride.getIsPaid()) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

        if (ride.getPaymentMethod() == PaymentMethodEnum.CASH) {
            PassengerPaymentRequestDto newPayment = rideMapper.fromRideToPassengerPaymentRequestDto(ride);
            PassengerPaymentResponseDto savedPayment = paymentClient.save(newPayment);

            paymentVerifier.verifyPaymentStatus(savedPayment);
        }

        ride.setIsPaid(true);
        ride.setStatus(RideStatusEnum.FINISHED);
        ride.setFinishRide(ZonedDateTime.now(CommonConstants.EUROPE_MINSK_TIMEZONE));

        RideResponseDto savedRide = rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));

        Message<RideResponseDto> message = MessageBuilder.withPayload(savedRide).build();
        streamBridge.send(OUT_FINISH_RIDE_CHANNEL, message);

        return savedRide;
    }

    @Transactional
    @Override
    public RideResponseDto applyPromoCode(String rideId, String promoCodeKeyword) {
        log.debug("Call for RideService.applyPromoCode() with rideId {} and promo code keyword '{}'", rideId,
                promoCodeKeyword);

        Ride ride = findRideByIdOrThrowException(rideId);

        rideVerifier.verifyApplyPromoCode(ride);
        rideSecurityVerifier.verifyAllowedForTheRidePassenger(ride);

        PromoCodeResponseDto promoCodeDto = promoCodeService.getActivePromoCodeByKeyword(promoCodeKeyword);
        PromoCode promoCodeEntity = PromoCode.builder()
                .withId(promoCodeDto.id())
                .withKeyword(promoCodeDto.keyword())
                .withDiscountPercent(promoCodeDto.discountPercent())
                .withIsActive(promoCodeDto.isActive())
                .build();

        if (promoCodeEntity.equals(ride.getPromoCode())) {
            return rideMapper.fromEntityToResponseDto(ride);
        }

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

        rideVerifier.verifyChangePaymentMethod(ride);
        rideSecurityVerifier.verifyAllowedForTheRidePassenger(ride);

        PaymentMethodEnum newPaymentMethod;
        try {
            newPaymentMethod = PaymentMethodEnum.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            String errorMessage = messageSource.getMessage(
                    INVALID_PAYMENT_METHOD_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalArgumentException(errorMessage);
        }

        if (ride.getPaymentMethod() == newPaymentMethod) {
            return rideMapper.fromEntityToResponseDto(ride);
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

        rideVerifier.verifyScoreDriver(ride);
        rideSecurityVerifier.verifyAllowedForTheRidePassenger(ride);

        ride.setDriverScore(score);
        return rideMapper.fromEntityToResponseDto(
                rideRepository.save(ride));
    }

    @Transactional
    @Override
    public RideResponseDto scorePassenger(String rideId, Integer score) {
        log.debug("Call for RideService.scorePassenger() with rideId {} and score {}", rideId, score);

        Ride ride = findRideByIdOrThrowException(rideId);

        rideVerifier.verifyScorePassenger(ride);
        rideSecurityVerifier.verifyAllowedForTheRideDriver(ride);

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
            throw new EntityNotFoundException(errorMessage);
        }
        rideRepository.deleteById(uuid);
    }

    @Transactional(readOnly = true)
    @Override
    public RideResponseDto getRideById(String id) {
        log.debug("Call for RideService.getRideById() with id {}", id);

        Ride ride = findRideByIdOrThrowException(id);

        rideSecurityVerifier.verifyAllowedForRidePassengerOrDriverOrAdmin(ride);

        return rideMapper.fromEntityToResponseDto(ride);
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<RideResponseDto> getRides(Pageable pageable, Map<String, String> params) {
        log.debug("Call for RideService.getRides() with pageable settings: {} and request parameters: {}", pageable,
                params);

        RidesFilterParams filterParams = filterParamsMapper.fromMapParams(params);
        Specification<Ride> spec = rideSpecs.getAllByFilter(filterParams);
        List<RideResponseDto> rides = rideRepository.findAll(spec, pageable).stream()
                .map(rideMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<RideResponseDto>builder()
                .withValues(rides)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(rideRepository.count(spec), pageable.getPageSize()))
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

}
