package by.arvisit.cabapp.passengerservice.service.impl;

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
import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengersFilterParams;
import by.arvisit.cabapp.passengerservice.mapper.PassengerMapper;
import by.arvisit.cabapp.passengerservice.mapper.PassengersFilterParamsMapper;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import by.arvisit.cabapp.passengerservice.persistence.repository.PassengerRepository;
import by.arvisit.cabapp.passengerservice.persistence.util.PassengerSpecs;
import by.arvisit.cabapp.passengerservice.service.PassengerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    private static final String EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.passengerservice.persistence.model.Passenger.email.UsernameAlreadyExistsException.template";
    private static final String FOUND_NO_ENTITY_BY_EMAIL_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.passengerservice.persistence.model.Passenger.email.EntityNotFoundException.template";
    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.passengerservice.persistence.model.Passenger.id.EntityNotFoundException.template";

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final PassengersFilterParamsMapper filterParamsMapper;
    private final MessageSource messageSource;
    private final PassengerSpecs passengerSpecs;
    private final KeycloakService keycloakService;

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<PassengerResponseDto> getPassengers(Pageable pageable, Map<String, String> params) {
        log.debug("Call for PassengerService.getPassengers() with pageable settings: {} and request parametes: {}",
                pageable, params);

        PassengersFilterParams filterParams = filterParamsMapper.fromMapParams(params);
        Specification<Passenger> spec = passengerSpecs.getAllByFilter(filterParams);
        List<PassengerResponseDto> passengers = passengerRepository.findAll(spec, pageable).stream()
                .map(passengerMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<PassengerResponseDto>builder()
                .withValues(passengers)
                .withCurrentPage(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .withLastPage(getLastPageNumber(passengerRepository.count(spec), pageable.getPageSize()))
                .withSort(pageable.getSort().toString())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public PassengerResponseDto getPassengerById(String id) {
        log.debug("Call for PassengerService.getPassengerById() with id {}", id);

        return passengerMapper.fromEntityToResponseDto(
                findPassengerByIdOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public PassengerResponseDto getPassengerByEmail(String email) {
        log.debug("Call for PassengerService.getPassengerByEmail() with email {}", email);

        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_EMAIL_MESSAGE_TEMPLATE_KEY,
                new Object[] { email }, null);
        return passengerMapper.fromEntityToResponseDto(
                passengerRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new EntityNotFoundException(errorMessage)));
    }

    @Transactional
    @Override
    public PassengerResponseDto save(PassengerRequestDto dto) {
        log.debug("Call for PassengerService.save() with dto {}", dto);

        if (passengerRepository.existsByEmail(dto.email())) {
            String errorMessage = messageSource.getMessage(
                    EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { dto.email() }, null);
            throw new UsernameAlreadyExistsException(errorMessage);
        }
        String id = keycloakService.addUser(dto);
        Passenger passengerToSave = passengerMapper.fromRequestDtoToEntity(dto);
        passengerToSave.setId(UUID.fromString(id));
        return passengerMapper.fromEntityToResponseDto(
                passengerRepository.save(passengerToSave));
    }

    @Transactional
    @Override
    public PassengerResponseDto update(String id, PassengerRequestDto dto) {
        log.debug("Call for PassengerService.update() with id {} and dto {}", id, dto);

        Passenger existingPassenger = findPassengerByIdOrThrowException(id);

        if (passengerRepository.existsByEmail(dto.email()) && !existingPassenger.getEmail().equals(dto.email())) {
            String emailInUseErrorMessage = messageSource.getMessage(
                    EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { dto.email() }, null);
            throw new UsernameAlreadyExistsException(emailInUseErrorMessage);
        }

        passengerMapper.updateEntityWithRequestDto(dto, existingPassenger);
        return passengerMapper.fromEntityToResponseDto(
                passengerRepository.save(existingPassenger));
    }

    @Transactional
    @Override
    public void delete(String id) {
        log.debug("Call for PassengerService.delete() with id {}", id);

        UUID uuid = UUID.fromString(id);
        if (!passengerRepository.existsById(uuid)) {
            String errorMessage = messageSource.getMessage(
                    FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                    new Object[] { id }, null);
            throw new EntityNotFoundException(errorMessage);
        }
        passengerRepository.deleteById(uuid);
    }

    private Passenger findPassengerByIdOrThrowException(String id) {
        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        return passengerRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new EntityNotFoundException(errorMessage));
    }

}
