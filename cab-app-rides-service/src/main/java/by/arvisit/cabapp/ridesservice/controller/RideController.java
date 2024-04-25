package by.arvisit.cabapp.ridesservice.controller;

import java.util.Map;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.validation.MapContainsAllowedKeys;
import by.arvisit.cabapp.common.validation.MapContainsKey;
import by.arvisit.cabapp.common.validation.MapContainsParseableDateValues;
import by.arvisit.cabapp.common.validation.MapContainsParseableUUIDValues;
import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.service.RatingService;
import by.arvisit.cabapp.ridesservice.service.RideService;
import by.arvisit.cabapp.ridesservice.util.AppConstants;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RideController {

    private static final String REQUEST_PARAMS_VALIDATION_NOT_ALLOWED_KEYS_MESSAGE = "{by.arvisit.cabapp.ridesservice.controller.RideController.requestParams.MapContainsAllowedKeys.message}";
    private static final String DRIVER_ID_KEY = "driverId";
    private static final String PROMO_CODE_KEYWORD_KEY = "promoCodeKeyword";
    private static final String PAYMENT_METHOD_KEY = "paymentMethod";
    private static final String DRIVER_SCORE_KEY = "driverScore";
    private static final String PASSENGER_SCORE_KEY = "passengerScore";
    private static final String PATCH_VALIDATION_SCORE_POSITIVE_OR_ZERO_MESSAGE_KEY = "{by.arvisit.cabapp.ridesservice.controller.RideController.patch.score.PositiveOrZero.message}";
    private static final String PATCH_VALIDATION_SCORE_MAX_MESSAGE_KEY = "{by.arvisit.cabapp.ridesservice.controller.RideController.patch.score.Max.message}";
    private static final String PATCH_VALIDATION_SCORE_NOT_NULL_MESSAGE_KEY = "{by.arvisit.cabapp.ridesservice.controller.RideController.patch.score.NotNull.message}";
    private static final String PATCH_VALIDATION_KEYWORD_NOT_BLANK_MESSAGE_KEY = "{by.arvisit.cabapp.ridesservice.controller.RideController.patch.keyword.NotBlank.message}";
    private static final String PATCH_VALIDATION_PAYMENT_METHOD_NOT_BLANK_MESSAGE_KEY = "{by.arvisit.cabapp.ridesservice.controller.RideController.patch.paymentMethod.NotBlank.message}";
    private static final String PATCH_VALIDATION_NOT_NULL_MESSAGE_KEY = "{by.arvisit.cabapp.ridesservice.controller.RideController.patch.NotNull.message}";
    private static final String PATCH_SIZE_VALIDATION_MESSAGE_KEY = "{by.arvisit.cabapp.ridesservice.controller.RideController.patch.Size.message}";
    private final RideService rideService;
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RideResponseDto> save(@RequestBody @Valid RideRequestDto dto) {
        RideResponseDto response = rideService.save(dto);

        log.debug("New ride was added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    public RideResponseDto cancelRide(@PathVariable @UUID String id) {
        RideResponseDto response = rideService.cancelRide(id);

        log.debug("Ride with id {} was canceled", id);
        return response;
    }

    @PatchMapping("/{id}/accept")
    public RideResponseDto acceptRide(@PathVariable @UUID String id,
            @RequestBody
            @NotNull(message = PATCH_VALIDATION_NOT_NULL_MESSAGE_KEY)
            @Valid
            @MapContainsKey(DRIVER_ID_KEY)
            @Size(min = 1, max = 1, message = PATCH_SIZE_VALIDATION_MESSAGE_KEY) Map<String, @UUID String> patch) {

        String driverId = patch.get(DRIVER_ID_KEY);

        RideResponseDto response = rideService.acceptRide(id, driverId);

        log.debug("Ride with id {} was accepted by driver with id {}", id, driverId);
        return response;
    }

    @PatchMapping("/{id}/begin")
    public RideResponseDto beginRide(@PathVariable @UUID String id) {
        RideResponseDto response = rideService.beginRide(id);

        log.debug("Ride with id {} was began", id);
        return response;
    }

    @PatchMapping("/{id}/end")
    public RideResponseDto endRide(@PathVariable @UUID String id) {
        RideResponseDto response = rideService.endRide(id);

        log.debug("Ride with id {} was ended", id);
        return response;
    }

    @PatchMapping("/{id}/finish")
    public RideResponseDto finishRide(@PathVariable @UUID String id) {
        RideResponseDto response = rideService.finishRide(id);

        log.debug("Ride with id {} was finished", id);
        return response;
    }

    @PatchMapping("/{id}/confirm-payment")
    public RideResponseDto confirmPayment(@PathVariable @UUID String id) {
        RideResponseDto response = rideService.confirmPayment(id);

        log.debug("Ride with id {} has received payment confirmation", id);
        return response;
    }

    @PatchMapping("/{id}/apply-promo")
    public RideResponseDto applyPromoCode(@PathVariable @UUID String id,
            @RequestBody
            @NotNull(message = PATCH_VALIDATION_NOT_NULL_MESSAGE_KEY)
            @Valid
            @MapContainsKey(PROMO_CODE_KEYWORD_KEY)
            @Size(min = 1, max = 1, message = PATCH_SIZE_VALIDATION_MESSAGE_KEY) Map<String, @NotBlank(
                    message = PATCH_VALIDATION_KEYWORD_NOT_BLANK_MESSAGE_KEY) String> patch) {

        String promoCodeKeyword = patch.get(PROMO_CODE_KEYWORD_KEY);

        RideResponseDto response = rideService.applyPromoCode(id, promoCodeKeyword);

        log.debug("Promo code '{}' was applied to the ride with id {}", promoCodeKeyword, id);
        return response;
    }

    @PatchMapping("/{id}/change-payment-method")
    public RideResponseDto changePaymentMethod(@PathVariable @UUID String id,
            @RequestBody
            @NotNull(message = PATCH_VALIDATION_NOT_NULL_MESSAGE_KEY)
            @Valid
            @MapContainsKey(PAYMENT_METHOD_KEY)
            @Size(min = 1, max = 1, message = PATCH_SIZE_VALIDATION_MESSAGE_KEY) Map<String, @NotBlank(
                    message = PATCH_VALIDATION_PAYMENT_METHOD_NOT_BLANK_MESSAGE_KEY) String> patch) {

        String paymentMethod = patch.get(PAYMENT_METHOD_KEY);

        RideResponseDto response = rideService.changePaymentMethod(id, paymentMethod);

        log.debug("Payment method was changed to '{}' for the ride with id {}", paymentMethod, id);
        return response;
    }

    @PatchMapping("/{id}/score-driver")
    public RideResponseDto scoreDriver(@PathVariable @UUID String id,
            @RequestBody
            @NotNull(message = PATCH_VALIDATION_NOT_NULL_MESSAGE_KEY)
            @Valid
            @MapContainsKey(DRIVER_SCORE_KEY)
            @Size(min = 1, max = 1, message = PATCH_SIZE_VALIDATION_MESSAGE_KEY) Map<String, @NotNull(
                    message = PATCH_VALIDATION_SCORE_NOT_NULL_MESSAGE_KEY) @Max(value = AppConstants.MAX_SCORE,
                            message = PATCH_VALIDATION_SCORE_MAX_MESSAGE_KEY) @PositiveOrZero(
                                    message = PATCH_VALIDATION_SCORE_POSITIVE_OR_ZERO_MESSAGE_KEY) Integer> patch) {

        Integer driverScore = patch.get(DRIVER_SCORE_KEY);

        RideResponseDto response = rideService.scoreDriver(id, driverScore);

        log.debug("For the ride with id {} driver got score: {}", id, driverScore);
        return response;
    }

    @PatchMapping("/{id}/score-passenger")
    public RideResponseDto scorePassenger(@PathVariable @UUID String id,
            @RequestBody
            @NotNull(message = PATCH_VALIDATION_NOT_NULL_MESSAGE_KEY)
            @Valid
            @MapContainsKey(PASSENGER_SCORE_KEY)
            @Size(min = 1, max = 1, message = PATCH_SIZE_VALIDATION_MESSAGE_KEY) Map<String, @NotNull(
                    message = PATCH_VALIDATION_SCORE_NOT_NULL_MESSAGE_KEY) @Max(value = AppConstants.MAX_SCORE,
                            message = PATCH_VALIDATION_SCORE_MAX_MESSAGE_KEY) @PositiveOrZero(
                                    message = PATCH_VALIDATION_SCORE_POSITIVE_OR_ZERO_MESSAGE_KEY) Integer> patch) {

        Integer passengerScore = patch.get(PASSENGER_SCORE_KEY);

        RideResponseDto response = rideService.scorePassenger(id, passengerScore);

        log.debug("For the ride with id {} passenger got score: {}", id, passengerScore);
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @UUID String id) {
        rideService.delete(id);

        log.debug("Ride with id {} was removed", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public RideResponseDto getRidebyId(@PathVariable @UUID String id) {
        RideResponseDto response = rideService.getRideById(id);

        log.debug("Got ride with id {}: {}", response);
        return response;
    }

    @GetMapping
    public ListContainerResponseDto<RideResponseDto> getRides(@PageableDefault @Nullable @Valid Pageable pageable,
            @RequestParam @Nullable
            @MapContainsAllowedKeys(
                    keys = { "page", "size", "sort", "startAddress", "destinationAddress", "status", "paymentMethod",
                            "passengerId", "driverId", "bookRide", "cancelRide", "acceptRide", "beginRide", "endRide",
                            "finishRide" },
                    message = REQUEST_PARAMS_VALIDATION_NOT_ALLOWED_KEYS_MESSAGE)
            @MapContainsParseableUUIDValues(
                    keys = { "passengerId", "driverId" })
            @MapContainsParseableDateValues(
                    keys = { "driverId", "bookRide", "cancelRide", "acceptRide", "beginRide", "endRide",
                            "finishRide" }) Map<String, @NotBlank String> requestParams) {
        log.debug("Get all rides according to request parameters: {}", requestParams);
        ListContainerResponseDto<RideResponseDto> response = rideService.getRides(pageable, requestParams);

        log.debug("Got all rides. Total count: {}. Pageable settings: {}", response.values().size(), pageable);
        return response;
    }

    @GetMapping("/passengers/{id}")
    public ListContainerResponseDto<RideResponseDto> getRidesByPassengerId(@PathVariable @UUID String id,
            @PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<RideResponseDto> response = rideService.getRidesByPassengerId(id, pageable);

        log.debug("Got all rides for passenger with id {}. Total count: {}. Pageable settings: {}", id,
                response.values().size(), pageable);
        return response;
    }

    @GetMapping("/drivers/{id}")
    public ListContainerResponseDto<RideResponseDto> getRidesByDriverId(@PathVariable @UUID String id,
            @PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<RideResponseDto> response = rideService.getRidesByDriverId(id, pageable);

        log.debug("Got all rides for driver with id {}. Total count: {}. Pageable settings: {}", id,
                response.values().size(), pageable);
        return response;
    }

    @GetMapping("/passengers/{id}/rating")
    public RatingResponseDto getPassengerRating(@PathVariable @UUID String id) {
        RatingResponseDto response = ratingService.getPassengerRating(id);

        log.debug("Got rating '{}' for passenger with id {}", response.rating(), id);
        return response;
    }

    @GetMapping("/drivers/{id}/rating")
    public RatingResponseDto getDriverRating(@PathVariable @UUID String id) {
        RatingResponseDto response = ratingService.getDriverRating(id);

        log.debug("Got rating '{}' for driver with id {}", response.rating(), id);
        return response;
    }

}
