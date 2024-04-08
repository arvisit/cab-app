package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.ridesservice.util.PaginationUtil.getLastPageNumber;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.exceptionhandlingstarter.exception.ValueAlreadyInUseException;
import by.arvisit.cabapp.ridesservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.mapper.PromoCodeMapper;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.repository.PromoCodeRepository;
import by.arvisit.cabapp.ridesservice.service.PromoCodeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromoCodeServiceImpl implements PromoCodeService {

    private static final String PROMO_CODE_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.PromoCode.keyword.ValueAlreadyInUseException.template";
    private static final String PROMO_CODE_DEACTIVATED_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.PromoCode.active.IllegalStateException.template";
    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.PromoCode.id.EntityNotFoundException.template";
    private static final String FOUND_NO_ACTIVE_ENTITY_BY_KEYWORD_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.PromoCode.keyword.active.EntityNotFoundException.template";

    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;
    private final MessageSource messageSource;

    @Transactional(readOnly = true)
    @Override
    public PromoCodeResponseDto getPromoCodeById(Long id) {
        log.debug("Call for PromoCodeService.getPromoCodeById() with id {}", id);

        return promoCodeMapper.fromEntityToResponseDto(
                findPromoCodeByIdOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public PromoCodeResponseDto getActivePromoCodeByKeyword(String keyword) {
        log.debug("Call for PromoCodeService.getActivePromoCodeByKeyword() with keyword {}", keyword);

        String errorMessage = messageSource.getMessage(
                FOUND_NO_ACTIVE_ENTITY_BY_KEYWORD_MESSAGE_TEMPLATE_KEY,
                new Object[] { keyword }, null);
        return promoCodeMapper.fromEntityToResponseDto(
                promoCodeRepository.findByKeywordAndIsActiveTrue(keyword)
                        .orElseThrow(
                                () -> new EntityNotFoundException(errorMessage)));
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<PromoCodeResponseDto> getPromoCodes(Pageable pageable) {
        log.debug("Call for PromoCodeService.getPromoCodes() with pageable settings: {}", pageable);

        List<PromoCodeResponseDto> promoCodes = promoCodeRepository.findAll(pageable).stream()
                .map(promoCodeMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<PromoCodeResponseDto>builder()
                .withValues(promoCodes)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(promoCodeRepository.count(), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<PromoCodeResponseDto> getActivePromoCodes(Pageable pageable) {
        log.debug("Call for PromoCodeService.getIsActivePromoCodes() with pageable settings: {}", pageable);

        List<PromoCodeResponseDto> promoCodes = promoCodeRepository.findAllByIsActiveTrue(pageable).stream()
                .map(promoCodeMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<PromoCodeResponseDto>builder()
                .withValues(promoCodes)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(promoCodeRepository.countByIsActiveTrue(), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional
    @Override
    public PromoCodeResponseDto save(PromoCodeRequestDto dto) {
        log.debug("Call for PromoCodeService.save() with dto {}", dto);

        if (promoCodeRepository.existsByKeywordAndIsActiveTrue(dto.keyword())) {
            throwExceptionForInUsePromoCode(dto);
        }
        PromoCode promoCodeToSave = promoCodeMapper.fromRequestDtoToEntity(dto);
        promoCodeToSave.setIsActive(true);

        return promoCodeMapper.fromEntityToResponseDto(
                promoCodeRepository.save(promoCodeToSave));
    }

    @Transactional
    @Override
    public PromoCodeResponseDto update(Long id, PromoCodeRequestDto dto) {
        log.debug("Call for PromoCodeService.update() with id {} and dto {}", id, dto);

        PromoCode existingPromoCode = findPromoCodeByIdOrThrowException(id);

        if (!existingPromoCode.getIsActive()) {
            String errorMessage = messageSource.getMessage(
                    PROMO_CODE_DEACTIVATED_MESSAGE_TEMPLATE_KEY,
                    new Object[] { id }, null);
            throw new IllegalStateException(errorMessage);
        }

        if (promoCodeRepository.existsByKeywordAndIsActiveTrue(dto.keyword())
                && !existingPromoCode.getKeyword().equals(dto.keyword())) {
            throwExceptionForInUsePromoCode(dto);
        }

        promoCodeMapper.updateEntityWithRequestDto(dto, existingPromoCode);
        return promoCodeMapper.fromEntityToResponseDto(
                promoCodeRepository.save(existingPromoCode));
    }

    @Transactional
    @Override
    public PromoCodeResponseDto deactivatePromoCode(Long id) {
        log.debug("Call for PromoCodeService.deactivatePromoCode() with id {}", id);

        PromoCode existingPromoCode = findPromoCodeByIdOrThrowException(id);

        if (!existingPromoCode.getIsActive()) {
            return promoCodeMapper.fromEntityToResponseDto(existingPromoCode);
        }

        existingPromoCode.setIsActive(false);
        return promoCodeMapper.fromEntityToResponseDto(
                promoCodeRepository.save(existingPromoCode));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Call for PromoCodeService.delete() with id {}", id);

        if (!promoCodeRepository.existsById(id)) {
            String errorMessage = messageSource.getMessage(
                    FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                    new Object[] { id }, null);
            throw new EntityNotFoundException(errorMessage);
        }
        promoCodeRepository.deleteById(id);
    }

    private PromoCode findPromoCodeByIdOrThrowException(Long id) {
        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        return promoCodeRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(errorMessage));
    }

    private void throwExceptionForInUsePromoCode(PromoCodeRequestDto dto) {
        String errorMessage = messageSource.getMessage(
                PROMO_CODE_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY,
                new Object[] { dto.keyword() }, null);
        throw new ValueAlreadyInUseException(errorMessage);
    }

}
