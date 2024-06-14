package by.arvisit.cabapp.passengerservice.controller;

import java.util.Map;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.validation.AllowedKeys;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengersFilterParams;
import by.arvisit.cabapp.passengerservice.service.PassengerService;
import jakarta.annotation.Nullable;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerResponseDto> save(@RequestBody @Valid PassengerRequestDto dto) {
        PassengerResponseDto response = passengerService.save(dto);

        log.debug("New passenger was added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public PassengerResponseDto update(@PathVariable @UUID String id, @RequestBody @Valid PassengerRequestDto dto) {
        PassengerResponseDto response = passengerService.update(id, dto);

        log.debug("Passenger with id {} was updated and now is: {}", id, response);
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @UUID String id) {
        passengerService.delete(id);

        log.debug("Passenger with id {} was removed", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public PassengerResponseDto getPassengerById(@PathVariable @UUID String id) {
        PassengerResponseDto response = passengerService.getPassengerById(id);

        log.debug("Got passenger by id {}: {}", id, response);
        return response;
    }

    @GetMapping("/by-email/{email}")
    public PassengerResponseDto getPassengerByEmail(@PathVariable @Email String email) {
        PassengerResponseDto response = passengerService.getPassengerByEmail(email);

        log.debug("Got passenger by email {}: {}", email, response);
        return response;
    }

    @GetMapping
    @RolesAllowed("ADMIN")
    public ListContainerResponseDto<PassengerResponseDto> getPassengers(
            @PageableDefault @Nullable @Valid Pageable pageable,
            @RequestParam @Nullable
            @AllowedKeys(keysHolder = PassengersFilterParams.class)
            Map<String, @NotBlank String> requestParams) {

        log.debug("Get all passengers according to request parameters: {}", requestParams);
        ListContainerResponseDto<PassengerResponseDto> response = passengerService.getPassengers(pageable,
                requestParams);

        log.debug("Got all passengers. Total count: {}. Pageable settings: {}", response.values().size(), pageable);
        return response;
    }

}
