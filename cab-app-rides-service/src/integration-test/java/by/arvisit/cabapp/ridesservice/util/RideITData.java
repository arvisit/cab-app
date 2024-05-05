package by.arvisit.cabapp.ridesservice.util;

import java.util.Collections;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;

public final class RideITData {

    public static final String URL_PROMO_CODES = "/api/v1/promo-codes";
    public static final String URL_PROMO_CODES_ACTIVE = "/api/v1/promo-codes/active";
    public static final String URL_PROMO_CODES_ID_TEMPLATE = "/api/v1/promo-codes/{id}";
    public static final String URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE = "/api/v1/promo-codes/{id}/deactivate";

    public static final String URL_RIDES = "/api/v1/rides";
    public static final String URL_RIDES_ID_TEMPLATE = "/api/v1/rides/{id}";
    public static final String URL_RIDES_ID_CANCEL_TEMPLATE = "/api/v1/rides/{id}/cancel";
    public static final String URL_RIDES_ID_ACCEPT_TEMPLATE = "/api/v1/rides/{id}/accept";
    public static final String URL_RIDES_ID_BEGIN_TEMPLATE = "/api/v1/rides/{id}/begin";
    public static final String URL_RIDES_ID_END_TEMPLATE = "/api/v1/rides/{id}/end";
    public static final String URL_RIDES_ID_FINISH_TEMPLATE = "/api/v1/rides/{id}/finish";
    public static final String URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE = "/api/v1/rides/{id}/confirm-payment";
    public static final String URL_RIDES_ID_APPLY_PROMO_TEMPLATE = "/api/v1/rides/{id}/apply-promo";
    public static final String URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE = "/api/v1/rides/{id}/change-payment-method";
    public static final String URL_RIDES_ID_SCORE_DRIVER_TEMPLATE = "/api/v1/rides/{id}/score-driver";
    public static final String URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE = "/api/v1/rides/{id}/score-passenger";
    public static final String URL_RIDES_PASSENGER_ID_RATING_TEMPLATE = "/api/v1/rides/passengers/{id}/rating";
    public static final String URL_RIDES_DRIVER_ID_RATING_TEMPLATE = "/api/v1/rides/drivers/{id}/rating";
    public static final String URL_RIDES_PASSENGER_ID_TEMPLATE = "/api/v1/rides/passengers/{id}";
    public static final String URL_RIDES_DRIVER_ID_TEMPLATE = "/api/v1/rides/drivers/{id}";
    public static final String URL_RIDES_PARAM_VALUE_TEMPLATE = "/api/v1/rides?{param}={value}";

    public static final boolean NEW_PROMO_CODE_IS_ACTIVE = true;
    public static final long NEW_PROMO_CODE_ID = 1L;
    public static final String NEW_PROMO_CODE_KEYWORD = "PROMO";
    public static final int NEW_PROMO_CODE_DISCOUNT_PERCENT = 20;
    public static final int RICE23_DISCOUNT_PERCENT = 23;
    public static final String RICE23_KEYWORD = "RICE23";
    public static final boolean RICE23_IS_ACTIVE = false;
    public static final long RICE23_ID = 4L;
    public static final int PAIN49_NOT_ACTIVE_PERCENT_DISCOUNT = 49;
    public static final boolean PAIN49_NOT_ACTIVE_IS_ACTIVE = false;
    public static final long PAIN49_NOT_ACTIVE_ID = 2L;
    public static final int PAIN49_ACTIVE_DISCOUNT_PERCENT = 49;
    public static final String PAIN49_ACTIVE_KEYWORD = "PAIN49";
    public static final String PAIN49_NOT_ACTIVE_KEYWORD = "PAIN49";
    public static final boolean PAIN49_ACTIVE_IS_ACTIVE = true;
    public static final long PAIN49_ACTIVE_ID = 1L;
    public static final int BRILLIANT10_DISCOUNT_PERCENT = 10;
    public static final String BRILLIANT10_KEYWORD = "BRILLIANT10";
    public static final boolean BRILLIANT10_IS_ACTIVE = true;
    public static final long BRILLIANT10_ID = 3L;

    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";

    private RideITData() {
    }

    public static PromoCodeRequestDto.PromoCodeRequestDtoBuilder getNewPromoCodeRequestDto() {
        return PromoCodeRequestDto.builder()
                .withKeyword(NEW_PROMO_CODE_KEYWORD)
                .withDiscountPercent(NEW_PROMO_CODE_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getAddedPromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(null)
                .withIsActive(NEW_PROMO_CODE_IS_ACTIVE)
                .withKeyword(NEW_PROMO_CODE_KEYWORD)
                .withDiscountPercent(NEW_PROMO_CODE_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getBRILLIANT10ActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(BRILLIANT10_ID)
                .withIsActive(BRILLIANT10_IS_ACTIVE)
                .withKeyword(BRILLIANT10_KEYWORD)
                .withDiscountPercent(BRILLIANT10_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getPAIN49ActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(PAIN49_ACTIVE_ID)
                .withIsActive(PAIN49_ACTIVE_IS_ACTIVE)
                .withKeyword(PAIN49_ACTIVE_KEYWORD)
                .withDiscountPercent(PAIN49_ACTIVE_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getPAIN49NotActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(PAIN49_NOT_ACTIVE_ID)
                .withIsActive(PAIN49_NOT_ACTIVE_IS_ACTIVE)
                .withKeyword(PAIN49_NOT_ACTIVE_KEYWORD)
                .withDiscountPercent(PAIN49_NOT_ACTIVE_PERCENT_DISCOUNT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getRICE23NotActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(RICE23_ID)
                .withIsActive(RICE23_IS_ACTIVE)
                .withKeyword(RICE23_KEYWORD)
                .withDiscountPercent(RICE23_DISCOUNT_PERCENT);
    }

    public static <T> ListContainerResponseDto.ListContainerResponseDtoBuilder<T> getListContainerForResponse(
            Class<T> clazz) {
        return ListContainerResponseDto.<T>builder()
                .withValues(Collections.emptyList())
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED);
    }
}
