package by.arvisit.cabapp.ridesservice.mapper;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_NEW_DISCOUNT_PERCENT;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCode;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;

class PromoCodeMapperTest {

    private static final String IS_ACTIVE_FIELD = "isActive";
    private static final String ID_FIELD = "id";

    private PromoCodeMapper promoCodeMapper = Mappers.getMapper(PromoCodeMapper.class);

    @Test
    void shouldMapFromEntityToResponseDto() {
        PromoCode entity = getPromoCode().build();

        PromoCodeResponseDto actualResponseDto = promoCodeMapper.fromEntityToResponseDto(entity);
        PromoCodeResponseDto expectedResponseDto = getPromoCodeResponseDto().build();

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }

    @Test
    void shouldMapFromRequestDtoToEntity() {
        PromoCodeRequestDto requestDto = getPromoCodeRequestDto().build();

        PromoCode actualEntity = promoCodeMapper.fromRequestDtoToEntity(requestDto);
        PromoCode expectedEntity = getPromoCode().build();

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, IS_ACTIVE_FIELD)
                .isEqualTo(expectedEntity);
    }

    @Test
    void shouldUpdateEntityWithRequestDto() {
        PromoCodeRequestDto requestDto = getPromoCodeRequestDto()
                .withDiscountPercent(PROMO_CODE_NEW_DISCOUNT_PERCENT)
                .build();
        PromoCode entityToUpdate = getPromoCode().build();

        promoCodeMapper.updateEntityWithRequestDto(requestDto, entityToUpdate);

        assertThat(entityToUpdate.getDiscountPercent())
                .isEqualTo(requestDto.discountPercent());
    }
}
