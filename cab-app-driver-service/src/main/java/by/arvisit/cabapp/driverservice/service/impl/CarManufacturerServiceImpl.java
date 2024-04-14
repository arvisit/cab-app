package by.arvisit.cabapp.driverservice.service.impl;


import static by.arvisit.cabapp.common.util.PaginationUtil.getLastPageNumber;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.mapper.CarManufacturerMapper;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;
import by.arvisit.cabapp.driverservice.persistence.repository.CarManufacturerRepository;
import by.arvisit.cabapp.driverservice.service.CarManufacturerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarManufacturerServiceImpl implements CarManufacturerService {

    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer.id.EntityNotFoundException.template";

    private final CarManufacturerRepository carManufacturerRepository;
    private final CarManufacturerMapper carManufacturerMapper;
    private final MessageSource messageSource;

    @Transactional(readOnly = true)
    @Override
    public CarManufacturer getCarManufacturerEntityById(Integer id) {
        log.debug("Call for CarManufacturerService.getCarManufacturerEntityById() with id {}", id);

        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        return carManufacturerRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(errorMessage));
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<CarManufacturerResponseDto> getCarManufacturers(Pageable pageable) {
        log.debug("Call for CarManufacturerService.getCarManufacturers() with pageable settings: {}", pageable);

        List<CarManufacturerResponseDto> carManufacturers = carManufacturerRepository.findAll(pageable).stream()
                .map(carManufacturerMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<CarManufacturerResponseDto>builder()
                .withValues(carManufacturers)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(carManufacturerRepository.count(), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Override
    public boolean existsById(Integer id) {
        log.debug("Call for CarManufacturerService.existsById() with id {}", id);

        return carManufacturerRepository.existsById(id);
    }

}
