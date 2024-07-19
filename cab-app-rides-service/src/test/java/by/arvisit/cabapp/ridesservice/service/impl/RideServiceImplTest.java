package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.INVALID_PAYMENT_METHOD;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_DEFAULT_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_DRIVER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_DRIVER_SCORE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_INITIAL_COST;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PASSENGER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PASSENGER_SCORE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PAYMENT_METHOD_ENUM;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PAYMENT_METHOD_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getAcceptedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getAcceptedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBeganRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBeganRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBookedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBookedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getCanceledRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getCanceledRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getEndedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getEndedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedWithScoresRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedWithScoresRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPassengerPaymentRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPassengerPaymentResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPassengerResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCode;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getRideRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getRideResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.client.DriverClient;
import by.arvisit.cabapp.ridesservice.client.PassengerClient;
import by.arvisit.cabapp.ridesservice.client.PaymentClient;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.mapper.RideMapper;
import by.arvisit.cabapp.ridesservice.mapper.RidesFilterParamsMapper;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import by.arvisit.cabapp.ridesservice.persistence.util.RideSpecs;
import by.arvisit.cabapp.ridesservice.service.CostService;
import by.arvisit.cabapp.ridesservice.service.PromoCodeService;
import by.arvisit.cabapp.ridesservice.util.PaymentVerifier;
import by.arvisit.cabapp.ridesservice.util.RideTestData;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    private static final String FINISH_RIDE_FIELD = "finishRide";
    private static final String END_RIDE_FIELD = "endRide";
    private static final String BEGIN_RIDE_FIELD = "beginRide";
    private static final String ACCEPT_RIDE_FIELD = "acceptRide";
    private static final String CANCEL_RIDE_FIELD = "cancelRide";
    private static final String BOOK_RIDE_FIELD = "bookRide";
    @InjectMocks
    private RideServiceImpl rideService;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private RideMapper rideMapper;
    @Mock
    private RidesFilterParamsMapper filterParamsMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private CostService costService;
    @Mock
    private PromoCodeService promoCodeService;
    @Mock
    private RideVerifier rideVerifier;
    @Mock
    private RideSecurityVerifier rideSecurityVerifier;
    @Mock
    private PaymentClient paymentClient;
    @Mock
    private PassengerClient passengerClient;
    @Mock
    private StreamBridge streamBridge;
    @Mock
    private PaymentVerifier paymentVerifier;
    @Mock
    private DriverClient driverClient;
    @Mock
    private RideSpecs rideSpecs;

    @Test
    void shouldReturnRideResponseDto_whenSaveVerifiedRide() {
        Ride ride = getBookedRide().build();
        RideRequestDto rideRequestDto = getRideRequestDto().build();
        RideResponseDto rideResponseDto = getBookedRideResponseDto().build();

        when(rideMapper.fromRequestDtoToEntity(any(RideRequestDto.class)))
                .thenReturn(ride);
        doNothing().when(rideVerifier)
                .verifyCreateRide(any(Ride.class));
        when(costService.calculateInitialRideCost(anyString(), anyString()))
                .thenReturn(RIDE_DEFAULT_INITIAL_COST);
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(ride);

        RideResponseDto actual = rideService.save(rideRequestDto);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BOOK_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.bookRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenCancelVerifiedBookedRide() {
        Ride rideFromRepository = getBookedRide().build();
        Ride savedRide = getCanceledRide().build();
        RideResponseDto rideResponseDto = getCanceledRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyCancelRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.cancelRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(CANCEL_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.cancelRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenCancelVerifiedCanceledRide() {
        Ride rideFromRepository = getCanceledRide().build();
        RideResponseDto rideResponseDto = getCanceledRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyCancelRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        RideResponseDto actual = rideService.cancelRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(CANCEL_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.cancelRide())
                .isNotNull()
                .isEqualTo(rideFromRepository.getCancelRide());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenCancelNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.cancelRide(RIDE_DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenAcceptVerifiedBookedRide() {
        Ride rideFromRepository = getBookedRide().build();
        Ride savedRide = getAcceptedRide().build();
        RideResponseDto rideResponseDto = getAcceptedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyAcceptRide(any(Ride.class), anyString());
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.acceptRide(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_DRIVER_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ACCEPT_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.acceptRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenAcceptVerifiedAcceptedRide() {
        Ride rideFromRepository = getAcceptedRide().build();
        RideResponseDto rideResponseDto = getAcceptedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyAcceptRide(any(Ride.class), anyString());
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        RideResponseDto actual = rideService.acceptRide(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_DRIVER_ID_STRING);

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ACCEPT_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.acceptRide())
                .isNotNull()
                .isEqualTo(rideFromRepository.getAcceptRide());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenAcceptNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.acceptRide(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_DRIVER_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenBeginVerifiedAcceptedRide() {
        Ride rideFromRepository = getAcceptedRide().build();
        Ride savedRide = getBeganRide().build();
        RideResponseDto rideResponseDto = getBeganRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyBeginRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.beginRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BEGIN_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.beginRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenBeginVerifiedBeganRide() {
        Ride rideFromRepository = getBeganRide().build();
        RideResponseDto rideResponseDto = getBeganRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyBeginRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        RideResponseDto actual = rideService.beginRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BEGIN_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.beginRide())
                .isNotNull()
                .isEqualTo(rideFromRepository.getBeginRide());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenBeginNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.beginRide(RIDE_DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenEndVerifiedBeganRide() {
        Ride rideFromRepository = getBeganRide().build();
        Ride savedRide = getEndedRide().build();
        RideResponseDto rideResponseDto = getEndedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyEndRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);
        when(passengerClient.getPassengerById(anyString()))
                .thenReturn(getPassengerResponseDto().build());
        when(rideMapper.fromRideToPassengerPaymentRequestDto(any(Ride.class), anyString()))
                .thenReturn(getPassengerPaymentRequestDto().build());

        RideResponseDto actual = rideService.endRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(END_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.endRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenEndVerifiedBeganRideAndPayWithCash() {
        PaymentMethodEnum cashPaymentMethod = PaymentMethodEnum.CASH;
        Ride rideFromRepository = getBeganRide()
                .withPaymentMethod(cashPaymentMethod)
                .build();
        Ride savedRide = getEndedRide()
                .withPaymentMethod(cashPaymentMethod)
                .build();
        RideResponseDto rideResponseDto = getEndedRideResponseDto()
                .withPaymentMethod(cashPaymentMethod.toString())
                .build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyEndRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.endRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(END_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.endRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenEndVerifiedEndedRide() {
        Ride rideFromRepository = getEndedRide().build();
        RideResponseDto rideResponseDto = getEndedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyEndRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        RideResponseDto actual = rideService.endRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(END_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.endRide())
                .isNotNull()
                .isEqualTo(rideFromRepository.getEndRide());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenEndNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.endRide(RIDE_DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenFinishVerifiedEndedRide() {
        Ride rideFromRepository = getEndedRide().build();
        Ride savedRide = getFinishedRide().build();
        RideResponseDto rideResponseDto = getFinishedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyFinishRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.finishRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(FINISH_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.finishRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenFinishVerifiedFinishedRide() {
        Ride rideFromRepository = getFinishedRide().build();
        RideResponseDto rideResponseDto = getFinishedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyFinishRide(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        RideResponseDto actual = rideService.finishRide(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(FINISH_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.finishRide())
                .isNotNull()
                .isEqualTo(rideFromRepository.getFinishRide());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenFinishNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.finishRide(RIDE_DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenConfirmPaymentVerifiedEndedRide() {
        Ride rideFromRepository = getEndedRide().build();
        Ride savedRide = getFinishedRide().build();
        RideResponseDto rideResponseDto = getFinishedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyConfirmPayment(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.confirmPayment(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(FINISH_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.finishRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenConfirmPaymentVerifiedEndedRideAndPayWithCash() {
        PaymentMethodEnum cashPaymentMethod = PaymentMethodEnum.CASH;
        Ride rideFromRepository = getEndedRide()
                .withPaymentMethod(cashPaymentMethod)
                .build();
        Ride savedRide = getFinishedRide()
                .withPaymentMethod(cashPaymentMethod)
                .build();
        RideResponseDto rideResponseDto = getFinishedRideResponseDto()
                .withPaymentMethod(cashPaymentMethod.toString())
                .build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyConfirmPayment(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);
        when(rideMapper.fromRideToPassengerPaymentRequestDto(any(Ride.class)))
                .thenReturn(getPassengerPaymentRequestDto().build());
        when(paymentClient.save(any(PassengerPaymentRequestDto.class)))
                .thenReturn(getPassengerPaymentResponseDto().build());
        doNothing().when(paymentVerifier)
                .verifyPaymentStatus(any(PassengerPaymentResponseDto.class));

        RideResponseDto actual = rideService.confirmPayment(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(FINISH_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.finishRide())
                .isNotNull();
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenConfirmPaymentVerifiedFinishedRide() {
        Ride rideFromRepository = getFinishedRide().build();
        RideResponseDto rideResponseDto = getFinishedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyConfirmPayment(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        RideResponseDto actual = rideService.confirmPayment(RIDE_DEFAULT_ID_STRING);

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(FINISH_RIDE_FIELD)
                .isEqualTo(rideResponseDto);
        assertThat(actual.finishRide())
                .isNotNull()
                .isEqualTo(rideFromRepository.getFinishRide());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenConfirmPaymentForNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.confirmPayment(RIDE_DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenApplyPromoCodeToVerifiedEndedRide() {
        Ride rideFromRepository = getEndedRide().build();
        PromoCode promoCode = getPromoCode().build();
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto().build();
        Ride savedRide = getEndedRide()
                .withPromoCode(promoCode)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .build();
        RideResponseDto rideResponseDto = getEndedRideResponseDto()
                .withPromoCode(PROMO_CODE_DEFAULT_KEYWORD)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyApplyPromoCode(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);
        when(promoCodeService.getActivePromoCodeByKeyword(anyString()))
                .thenReturn(promoCodeResponseDto);
        when(costService.calculateRideCostWithPromoCode(any(BigDecimal.class), any(PromoCode.class)))
                .thenReturn(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE);

        RideResponseDto actual = rideService.applyPromoCode(RIDE_DEFAULT_ID_STRING, PROMO_CODE_DEFAULT_KEYWORD);

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(rideResponseDto);
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenApplyPromoCodeToRideWithSamePromoCode() {
        PromoCode promoCode = getPromoCode().build();
        Ride rideFromRepository = getEndedRide()
                .withPromoCode(promoCode)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .build();
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto().build();
        RideResponseDto rideResponseDto = getEndedRideResponseDto()
                .withPromoCode(PROMO_CODE_DEFAULT_KEYWORD)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyApplyPromoCode(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(promoCodeService.getActivePromoCodeByKeyword(anyString()))
                .thenReturn(promoCodeResponseDto);

        RideResponseDto actual = rideService.applyPromoCode(RIDE_DEFAULT_ID_STRING, PROMO_CODE_DEFAULT_KEYWORD);

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(rideResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenApplyPromoCodeToNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.applyPromoCode(RIDE_DEFAULT_ID_STRING, PROMO_CODE_DEFAULT_KEYWORD))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndCallRepository_whenChangePaymentMethodOnVerifiedEndedRide() {
        PaymentMethodEnum oldPaymentMethod = PaymentMethodEnum.CASH;
        Ride rideFromRepository = getEndedRide()
                .withPaymentMethod(oldPaymentMethod)
                .build();
        PaymentMethodEnum newPaymentMethod = RIDE_DEFAULT_PAYMENT_METHOD_ENUM;
        Ride savedRide = getEndedRide()
                .withPaymentMethod(newPaymentMethod)
                .build();
        RideResponseDto rideResponseDto = getEndedRideResponseDto()
                .withPaymentMethod(newPaymentMethod.toString())
                .build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyChangePaymentMethod(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.changePaymentMethod(RIDE_DEFAULT_ID_STRING, newPaymentMethod.toString());

        verify(rideRepository, times(1)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(rideResponseDto);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenChangePaymentMethodToInvalidValue() {
        Ride rideFromRepository = getEndedRide().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyChangePaymentMethod(any(Ride.class));

        assertThatThrownBy(() -> rideService.changePaymentMethod(RIDE_DEFAULT_ID_STRING, INVALID_PAYMENT_METHOD))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReturnRideResponseDtoAndNotCallRepository_whenChangePaymentMethodOnVerifiedEndedRideWithSameValue() {
        PaymentMethodEnum oldPaymentMethod = RIDE_DEFAULT_PAYMENT_METHOD_ENUM;
        Ride rideFromRepository = getEndedRide()
                .withPaymentMethod(oldPaymentMethod)
                .build();
        PaymentMethodEnum newPaymentMethod = RIDE_DEFAULT_PAYMENT_METHOD_ENUM;
        RideResponseDto rideResponseDto = getEndedRideResponseDto()
                .withPaymentMethod(newPaymentMethod.toString())
                .build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyChangePaymentMethod(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        RideResponseDto actual = rideService.changePaymentMethod(RIDE_DEFAULT_ID_STRING, newPaymentMethod.toString());

        verify(rideRepository, times(0)).save(any(Ride.class));

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(rideResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenChangePaymentMethodOnNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> rideService.changePaymentMethod(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_PAYMENT_METHOD_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDto_whenScoreDriverOfVerifiedEndedRide() {
        Ride rideFromRepository = getFinishedRide().build();
        Ride savedRide = getFinishedWithScoresRide().build();
        RideResponseDto rideResponseDto = getFinishedWithScoresRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyScoreDriver(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.scoreDriver(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_DRIVER_SCORE);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(rideResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenScoreDriverOfNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> rideService.scoreDriver(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_DRIVER_SCORE))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDto_whenScorePassengerOfVerifiedEndedRide() {
        Ride rideFromRepository = getFinishedRide().build();
        Ride savedRide = getFinishedWithScoresRide().build();
        RideResponseDto rideResponseDto = getFinishedWithScoresRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(rideFromRepository));
        doNothing().when(rideVerifier)
                .verifyScorePassenger(any(Ride.class));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponseDto actual = rideService.scorePassenger(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_PASSENGER_SCORE);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(rideResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenScorePassengerOfNonExistingRide() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> rideService.scorePassenger(RIDE_DEFAULT_ID_STRING, RIDE_DEFAULT_PASSENGER_SCORE))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowNothing_whenDeleteExistingRide() {
        when(rideRepository.existsById(any(UUID.class)))
                .thenReturn(true);
        doNothing().when(rideRepository)
                .deleteById(any(UUID.class));

        assertThatNoException()
                .isThrownBy(() -> rideService.delete(RIDE_DEFAULT_ID_STRING));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeleteNonExistingRide() {
        when(rideRepository.existsById(any(UUID.class)))
                .thenReturn(false);

        assertThatThrownBy(() -> rideService.delete(RIDE_DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRideResponseDto_whenGetRideByExistingId() {
        Ride ride = getBookedRide().build();
        RideResponseDto rideResponseDto = getBookedRideResponseDto().build();

        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(ride));
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);

        assertThat(rideService.getRideById(RIDE_DEFAULT_ID_STRING))
                .isEqualTo(rideResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetRideByNonExistingId() {
        when(rideRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.getRideById(RIDE_DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnContainerWithRideResponseDto_whenGetRides() {
        RideResponseDto rideResponseDto = getFinishedRideResponseDto().build();
        Ride ride = getFinishedRide().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<Ride> rides = List.of(ride);
        Page<Ride> ridesPage = new PageImpl<>(rides);
        Specification<Ride> spec = (root, query, cb) -> cb.conjunction();

        when(filterParamsMapper.fromMapParams(any()))
                .thenReturn(RideTestData.getEmptyRidesFilterParams().build());
        when(rideSpecs.getAllByFilter(any()))
                .thenReturn(spec);
        when(rideRepository.findAll(spec, pageable))
                .thenReturn(ridesPage);
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.count(spec))
                .thenReturn((long) rides.size());

        assertThat(rideService.getRides(pageable, any()))
                .usingRecursiveComparison()
                .isEqualTo(getRideResponseDtoInListContainer().build());
    }

    @Test
    void shouldReturnContainerWithRideResponseDto_whenGetRidesByPassengerId() {
        RideResponseDto rideResponseDto = getFinishedRideResponseDto().build();
        Ride ride = getFinishedRide().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<Ride> rides = List.of(ride);

        when(rideRepository.findByPassengerId(any(UUID.class), any(Pageable.class)))
                .thenReturn(rides);
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.countByPassengerId(any(UUID.class)))
                .thenReturn((long) rides.size());

        assertThat(rideService.getRidesByPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING, pageable))
                .usingRecursiveComparison()
                .isEqualTo(getRideResponseDtoInListContainer().build());
    }

    @Test
    void shouldReturnContainerWithRideResponseDto_whenGetRidesByDriverId() {
        RideResponseDto rideResponseDto = getFinishedRideResponseDto().build();
        Ride ride = getFinishedRide().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<Ride> rides = List.of(ride);

        when(rideRepository.findByDriverId(any(UUID.class), any(Pageable.class)))
                .thenReturn(rides);
        when(rideMapper.fromEntityToResponseDto(any(Ride.class)))
                .thenReturn(rideResponseDto);
        when(rideRepository.countByDriverId(any(UUID.class)))
                .thenReturn((long) rides.size());

        assertThat(rideService.getRidesByDriverId(RIDE_DEFAULT_DRIVER_ID_STRING, pageable))
                .usingRecursiveComparison()
                .isEqualTo(getRideResponseDtoInListContainer().build());
    }

}
