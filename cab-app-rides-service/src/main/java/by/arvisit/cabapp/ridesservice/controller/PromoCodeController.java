package by.arvisit.cabapp.ridesservice.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.ridesservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.service.PromoCodeService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/promo-codes")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping
    public ResponseEntity<PromoCodeResponseDto> save(@RequestBody @Valid PromoCodeRequestDto dto) {
        PromoCodeResponseDto response = promoCodeService.save(dto);

        log.debug("New promo code was added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public PromoCodeResponseDto update(@PathVariable Long id, @RequestBody @Valid PromoCodeRequestDto dto) {
        PromoCodeResponseDto response = promoCodeService.update(id, dto);

        log.debug("Promo code with id {} was updated and now is: {}", id, response);
        return response;
    }

    @PatchMapping("/{id}/deactivate")
    public PromoCodeResponseDto deactivate(@PathVariable Long id) {
        PromoCodeResponseDto response = promoCodeService.deactivatePromoCode(id);

        log.debug("Promo code with id {} was deactivated", id);
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        promoCodeService.delete(id);

        log.debug("Promo code with id {} was removed", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public PromoCodeResponseDto getPromoCodeById(@PathVariable Long id) {
        PromoCodeResponseDto response = promoCodeService.getPromoCodeById(id);

        log.debug("Got promo code by id {}: {}", id, response);
        return response;
    }

    @GetMapping
    public ListContainerResponseDto<PromoCodeResponseDto> getPromoCodes(
            @PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<PromoCodeResponseDto> response = promoCodeService.getPromoCodes(pageable);

        log.debug("Got all promo codes. Total count: {}. Pageable settings: {}", response.values().size(), pageable);
        return response;
    }

    @GetMapping("/active")
    public ListContainerResponseDto<PromoCodeResponseDto> getActivePromoCodes(
            @PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<PromoCodeResponseDto> response = promoCodeService.getActivePromoCodes(pageable);

        log.debug("Got all active promo codes. Total count: {}. Pageable settings: {}", response.values().size(),
                pageable);
        return response;
    }
}
