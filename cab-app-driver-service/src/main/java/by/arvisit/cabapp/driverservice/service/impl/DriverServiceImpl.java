package by.arvisit.cabapp.driverservice.service.impl;

import static by.arvisit.cabapp.common.util.PaginationUtil.getLastPageNumber;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.mapper.DriverMapper;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import by.arvisit.cabapp.driverservice.persistence.repository.DriverRepository;
import by.arvisit.cabapp.driverservice.persistence.util.DriverSpecs;
import by.arvisit.cabapp.driverservice.service.DriverService;
import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private static final String EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.driverservice.persistence.model.Driver.email.UsernameAlreadyExistsException.template";
    private static final String FOUND_NO_ENTITY_BY_EMAIL_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.driverservice.persistence.model.Driver.email.EntityNotFoundException.template";
    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.driverservice.persistence.model.Driver.id.EntityNotFoundException.template";

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final MessageSource messageSource;
    private final DriverSpecs driverSpecs;

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<DriverResponseDto> getDrivers(Pageable pageable, Map<String, String> params) {
        log.debug("Call for DriverService.getDrivers() with pageable settings: {} and request parameters: {}", pageable,
                params);

        Specification<Driver> spec = driverSpecs.getAllByFilter(params);
        List<DriverResponseDto> drivers = driverRepository.findAll(spec, pageable).stream()
                .map(driverMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<DriverResponseDto>builder()
                .withValues(drivers)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(driverRepository.count(spec), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<DriverResponseDto> getAvailableDrivers(Pageable pageable) {
        log.debug("Call for DriverService.getDrivers() with pageable settings: {}", pageable);

        List<DriverResponseDto> drivers = driverRepository.findByIsAvailableTrue(pageable).stream()
                .map(driverMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<DriverResponseDto>builder()
                .withValues(drivers)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(driverRepository.countByIsAvailableTrue(), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public DriverResponseDto getDriverById(String id) {
        log.debug("Call for DriverService.getDriverById() with id {}", id);

        return driverMapper.fromEntityToResponseDto(
                findDriverByIdOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public DriverResponseDto getDriverByEmail(String email) {
        log.debug("Call for DriverService.getDriverByEmail() with email {}", email);

        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_EMAIL_MESSAGE_TEMPLATE_KEY,
                new Object[] { email }, null);
        return driverMapper.fromEntityToResponseDto(
                driverRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new EntityNotFoundException(errorMessage)));
    }

    @Transactional
    @Override
    public DriverResponseDto save(DriverRequestDto dto) {
        log.debug("Call for DriverService.save() with dto {}", dto);

        if (driverRepository.existsByEmail(dto.email())) {
            String errorMessage = messageSource.getMessage(
                    EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { dto.email() }, null);
            throw new UsernameAlreadyExistsException(errorMessage);
        }

        Driver driverToSave = driverMapper.fromRequestDtoToEntity(dto);
        driverToSave.setIsAvailable(false);
        return driverMapper.fromEntityToResponseDto(
                driverRepository.save(driverToSave));
    }

    @Transactional
    @Override
    public DriverResponseDto update(String id, DriverRequestDto dto) {
        log.debug("Call for DriverService.update() with id {} and dto {}", id, dto);

        Driver existingDriver = findDriverByIdOrThrowException(id);

        if (driverRepository.existsByEmail(dto.email()) && !existingDriver.getEmail().equals(dto.email())) {
            String emailInUseErrorMessage = messageSource.getMessage(
                    EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { dto.email() }, null);
            throw new UsernameAlreadyExistsException(emailInUseErrorMessage);
        }

        driverMapper.updateEntityWithRequestDto(dto, existingDriver);
        return driverMapper.fromEntityToResponseDto(
                driverRepository.save(existingDriver));
    }

    @Transactional
    @Override
    public DriverResponseDto updateAvailability(String id, Boolean value) {
        log.debug("Call for DriverSerice.updateAvailability() with id {} and value {}", id, value);

        Driver existingDriver = findDriverByIdOrThrowException(id);

        if (existingDriver.getIsAvailable().equals(value)) {
            return driverMapper.fromEntityToResponseDto(existingDriver);
        }

        existingDriver.setIsAvailable(value);
        return driverMapper.fromEntityToResponseDto(
                driverRepository.save(existingDriver));
    }

    @Transactional
    @Override
    public void delete(String id) {
        log.debug("Call for DriverService.delete() with id {}", id);

        UUID uuid = UUID.fromString(id);
        if (!driverRepository.existsById(uuid)) {
            String errorMessage = messageSource.getMessage(
                    FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                    new Object[] { id }, null);
            throw new EntityNotFoundException(errorMessage);
        }
        driverRepository.deleteById(uuid);
    }

    private Driver findDriverByIdOrThrowException(String id) {
        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        return driverRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new EntityNotFoundException(errorMessage));
    }
}
