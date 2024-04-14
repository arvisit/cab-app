package by.arvisit.cabapp.driverservice.service.impl;

import static by.arvisit.cabapp.common.util.PaginationUtil.getLastPageNumber;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.mapper.ColorMapper;
import by.arvisit.cabapp.driverservice.persistence.model.Color;
import by.arvisit.cabapp.driverservice.persistence.repository.ColorRepository;
import by.arvisit.cabapp.driverservice.service.ColorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ColorServiceImpl implements ColorService {

    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.driverservice.persistence.model.Color.id.EntityNotFoundException.template";

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;
    private final MessageSource messageSource;

    @Transactional(readOnly = true)
    @Override
    public Color getColorEntityById(Integer id) {
        log.debug("Call for ColorService.getEntityColorById() with id {}", id);

        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        return colorRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(errorMessage));
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<ColorResponseDto> getColors(Pageable pageable) {
        log.debug("Call for ColorService.getColors() with pageable settings: {}", pageable);

        List<ColorResponseDto> colors = colorRepository.findAll(pageable).stream()
                .map(colorMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<ColorResponseDto>builder()
                .withValues(colors)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(colorRepository.count(), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Override
    public boolean existsById(Integer id) {
        log.debug("Call for ColorService.existsById() with id {}", id);

        return colorRepository.existsById(id);
    }

}
