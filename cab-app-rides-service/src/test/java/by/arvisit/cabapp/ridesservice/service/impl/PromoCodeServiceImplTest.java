package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_DEFAULT_ID;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_DEFAULT_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_NEW_DISCOUNT_PERCENT;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.PROMO_CODE_NEW_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCode;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCodeResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.exceptionhandlingstarter.exception.ValueAlreadyInUseException;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.mapper.PromoCodeMapper;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.repository.PromoCodeRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceImplTest {

    @InjectMocks
    private PromoCodeServiceImpl promoCodeService;
    @Mock
    private PromoCodeRepository promoCodeRepository;
    @Mock
    private PromoCodeMapper promoCodeMapper;
    @Mock
    private MessageSource messageSource;

    @Test
    void shouldReturnContainerWithPromoCodeResponseDto_whenGetPromoCodes() {
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto().build();
        PromoCode promoCode = getPromoCode().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<PromoCode> promoCodes = List.of(promoCode);
        Page<PromoCode> promoCodesPage = new PageImpl<>(promoCodes);

        when(promoCodeRepository.findAll(pageable))
                .thenReturn(promoCodesPage);
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);
        when(promoCodeRepository.count())
                .thenReturn((long) promoCodes.size());

        ListContainerResponseDto<PromoCodeResponseDto> expected = getPromoCodeResponseDtoInListContainer().build();

        assertThat(promoCodeService.getPromoCodes(pageable))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturnContainerWithActivePromoCodeResponseDto_whenGetActivePromoCodes() {
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto().build();
        PromoCode promoCode = getPromoCode().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<PromoCode> promoCodes = List.of(promoCode);

        when(promoCodeRepository.findAllByIsActiveTrue(pageable))
                .thenReturn(promoCodes);
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);
        when(promoCodeRepository.countByIsActiveTrue())
                .thenReturn((long) promoCodes.size());

        ListContainerResponseDto<PromoCodeResponseDto> expected = getPromoCodeResponseDtoInListContainer().build();

        assertThat(promoCodeService.getActivePromoCodes(pageable))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturnPromoCodeResponseDto_whenGetPromoCodeByExistingId() {
        PromoCode promoCode = getPromoCode().build();
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto().build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(promoCode));
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);

        assertThat(promoCodeService.getPromoCodeById(PROMO_CODE_DEFAULT_ID))
                .usingRecursiveComparison()
                .isEqualTo(promoCodeResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetPromoCodeByNonExistingId() {
        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> promoCodeService.getPromoCodeById(PROMO_CODE_DEFAULT_ID))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnPromoCodeResponseDto_whenGetActivePromoCodeByExistingKeyword() {
        PromoCode promoCode = getPromoCode().build();
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto().build();

        when(promoCodeRepository.findByKeywordAndIsActiveTrue(anyString()))
                .thenReturn(Optional.of(promoCode));
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);

        assertThat(promoCodeService.getActivePromoCodeByKeyword(PROMO_CODE_DEFAULT_KEYWORD))
                .usingRecursiveComparison()
                .isEqualTo(promoCodeResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetPromoCodeByNonExistingKeyword() {
        when(promoCodeRepository.findByKeywordAndIsActiveTrue(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> promoCodeService.getActivePromoCodeByKeyword(PROMO_CODE_DEFAULT_KEYWORD))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnPromoCodeResponseDto_whenSavePromoCodeWithNotInUseActiveKeyword() {
        PromoCode promoCode = getPromoCode().build();
        PromoCodeRequestDto promoCodeRequestDto = getPromoCodeRequestDto().build();
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto().build();

        when(promoCodeRepository.existsByKeywordAndIsActiveTrue(anyString()))
                .thenReturn(false);
        when(promoCodeMapper.fromRequestDtoToEntity(any(PromoCodeRequestDto.class)))
                .thenReturn(promoCode);
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);
        when(promoCodeRepository.save(any(PromoCode.class)))
                .thenReturn(promoCode);

        assertThat(promoCodeService.save(promoCodeRequestDto))
                .usingRecursiveComparison()
                .isEqualTo(promoCodeResponseDto);
    }

    @Test
    void shouldThrowValueAlreadyInUseException_whenSavePromoCodeWithInUseActiveKeyword() {
        PromoCodeRequestDto promoCodeRequestDto = getPromoCodeRequestDto().build();

        when(promoCodeRepository.existsByKeywordAndIsActiveTrue(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> promoCodeService.save(promoCodeRequestDto))
                .isInstanceOf(ValueAlreadyInUseException.class);
    }

    @Test
    void shouldReturnPromoCodeResponseDto_whenUpdateExistingPromoCodeWithSameKeywordAndNewDiscountPercent() {
        PromoCodeRequestDto promoCodeRequestDto = getPromoCodeRequestDto()
                .withDiscountPercent(PROMO_CODE_NEW_DISCOUNT_PERCENT)
                .build();
        PromoCode existingPromoCode = getPromoCode().build();
        PromoCode savedPromoCode = getPromoCode()
                .withDiscountPercent(PROMO_CODE_NEW_DISCOUNT_PERCENT)
                .build();
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto()
                .withDiscountPercent(PROMO_CODE_NEW_DISCOUNT_PERCENT)
                .build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(existingPromoCode));
        when(promoCodeRepository.existsByKeywordAndIsActiveTrue(anyString()))
                .thenReturn(true);
        doNothing().when(promoCodeMapper)
                .updateEntityWithRequestDto(any(PromoCodeRequestDto.class), any(PromoCode.class));
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);
        when(promoCodeRepository.save(any(PromoCode.class)))
                .thenReturn(savedPromoCode);

        assertThat(promoCodeService.update(PROMO_CODE_DEFAULT_ID, promoCodeRequestDto))
                .usingRecursiveComparison()
                .isEqualTo(promoCodeResponseDto);
    }

    @Test
    void shouldReturnPromoCodeResponseDto_whenUpdateExistingPromoCodeWithNewNotInActiveUseKeyword() {
        PromoCodeRequestDto promoCodeRequestDto = getPromoCodeRequestDto()
                .withKeyword(PROMO_CODE_NEW_KEYWORD)
                .build();
        PromoCode existingPromoCode = getPromoCode().build();
        PromoCode savedPromoCode = getPromoCode()
                .withKeyword(PROMO_CODE_NEW_KEYWORD)
                .build();
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto()
                .withKeyword(PROMO_CODE_NEW_KEYWORD)
                .build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(existingPromoCode));
        when(promoCodeRepository.existsByKeywordAndIsActiveTrue(anyString()))
                .thenReturn(false);
        doNothing().when(promoCodeMapper)
                .updateEntityWithRequestDto(any(PromoCodeRequestDto.class), any(PromoCode.class));
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);
        when(promoCodeRepository.save(any(PromoCode.class)))
                .thenReturn(savedPromoCode);

        assertThat(promoCodeService.update(PROMO_CODE_DEFAULT_ID, promoCodeRequestDto))
                .usingRecursiveComparison()
                .isEqualTo(promoCodeResponseDto);
    }

    @Test
    void shouldThrowValueAlreadyInUseException_whenUpdateExistingPromoCodeWithInUseKeywordOfAnotherActivePromoCode() {
        PromoCodeRequestDto promoCodeRequestDto = getPromoCodeRequestDto()
                .withKeyword(PROMO_CODE_NEW_KEYWORD)
                .build();
        PromoCode existingPromoCode = getPromoCode().build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(existingPromoCode));
        when(promoCodeRepository.existsByKeywordAndIsActiveTrue(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> promoCodeService.update(PROMO_CODE_DEFAULT_ID, promoCodeRequestDto))
                .isInstanceOf(ValueAlreadyInUseException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenUpdateExistingDeactivatedPromoCode() {
        PromoCodeRequestDto promoCodeRequestDto = getPromoCodeRequestDto()
                .withKeyword(PROMO_CODE_NEW_KEYWORD)
                .build();
        PromoCode existingPromoCode = getPromoCode()
                .withIsActive(false)
                .build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(existingPromoCode));

        assertThatThrownBy(() -> promoCodeService.update(PROMO_CODE_DEFAULT_ID, promoCodeRequestDto))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdateNonExistingPromoCode() {
        PromoCodeRequestDto promoCodeRequestDto = getPromoCodeRequestDto().build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> promoCodeService.update(PROMO_CODE_DEFAULT_ID, promoCodeRequestDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldCallRepository_whenDeactivateActivePromoCode() {
        boolean oldValue = true;
        PromoCode existingPromoCode = getPromoCode()
                .withIsActive(oldValue)
                .build();
        boolean newValue = false;
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto()
                .withIsActive(newValue)
                .build();
        PromoCode updatedPromoCode = getPromoCode()
                .withIsActive(newValue)
                .build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(existingPromoCode));
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);
        when(promoCodeRepository.save(any(PromoCode.class)))
                .thenReturn(updatedPromoCode);

        PromoCodeResponseDto actualResponseDto = promoCodeService.deactivatePromoCode(PROMO_CODE_DEFAULT_ID);

        verify(promoCodeRepository, times(1)).save(existingPromoCode);

        assertThat(actualResponseDto.isActive())
                .isEqualTo(newValue);
        assertThat(oldValue)
                .isNotEqualTo(newValue);
    }

    @Test
    void shouldNotCallRepository_whenDeactivateAlreadyDeactivatedPromoCode() {
        boolean oldValue = false;
        PromoCode existingPromoCode = getPromoCode()
                .withIsActive(oldValue)
                .build();
        boolean newValue = false;
        PromoCodeResponseDto promoCodeResponseDto = getPromoCodeResponseDto()
                .withIsActive(newValue)
                .build();

        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(existingPromoCode));
        when(promoCodeMapper.fromEntityToResponseDto(any(PromoCode.class)))
                .thenReturn(promoCodeResponseDto);

        PromoCodeResponseDto actualResponseDto = promoCodeService.deactivatePromoCode(PROMO_CODE_DEFAULT_ID);

        verify(promoCodeRepository, times(0)).save(existingPromoCode);

        assertThat(actualResponseDto.isActive())
                .isEqualTo(newValue);
        assertThat(oldValue)
                .isEqualTo(newValue);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeactivateNonExistingPromoCode() {
        when(promoCodeRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> promoCodeService.deactivatePromoCode(PROMO_CODE_DEFAULT_ID))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowNothing_whenDeleteExistingPromoCode() {
        when(promoCodeRepository.existsById(any(Long.class)))
                .thenReturn(true);
        doNothing().when(promoCodeRepository)
                .deleteById(any(Long.class));

        assertThatNoException()
                .isThrownBy(() -> promoCodeService.delete(PROMO_CODE_DEFAULT_ID));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeleteNonExistingPromoCode() {
        when(promoCodeRepository.existsById(any(Long.class)))
                .thenReturn(false);

        assertThatThrownBy(() -> promoCodeService.delete(PROMO_CODE_DEFAULT_ID))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
