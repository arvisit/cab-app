package by.arvisit.cabapp.ridesservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.ridesservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;

public interface PromoCodeService {

    PromoCodeResponseDto getPromoCodeById(Long id);

    PromoCodeResponseDto getActivePromoCodeByKeyword(String keyword);

    ListContainerResponseDto<PromoCodeResponseDto> getPromoCodes(Pageable pageable);

    ListContainerResponseDto<PromoCodeResponseDto> getActivePromoCodes(Pageable pageable);

    PromoCodeResponseDto save(PromoCodeRequestDto dto);

    PromoCodeResponseDto update(Long id, PromoCodeRequestDto dto);

    PromoCodeResponseDto deactivatePromoCode(Long id);

    void delete(Long id);

}
