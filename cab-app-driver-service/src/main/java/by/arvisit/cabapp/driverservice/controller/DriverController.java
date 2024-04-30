package by.arvisit.cabapp.driverservice.controller;

import java.util.Map;

import org.hibernate.validator.constraints.UUID;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.validation.MapContainsAllowedKeys;
import by.arvisit.cabapp.common.validation.MapContainsKey;
import by.arvisit.cabapp.common.validation.MapContainsParseableBooleanValues;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.service.DriverService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DriverController {

    private static final String IS_AVAILABLE_KEY = "isAvailable";
    private static final String REQUEST_PARAMS_VALIDATION_NOT_ALLOWED_KEYS_MESSAGE = "{by.arvisit.cabapp.driverservice.controller.DriverController.requestParams.MapContainsAllowedKeys.message}";
    private static final String PATCH_VALIDATION_SIZE_MESSAGE_KEY = "{by.arvisit.cabapp.driverservice.controller.DriverController.patch.Size.message}";
    private static final String PATCH_VALIDATION_AVAILABILITY_NOT_NULL_MESSAGE_KEY = "{by.arvisit.cabapp.driverservice.controller.DriverController.patch.isAvailable.NotNull.message}";
    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverResponseDto> save(@RequestBody @Valid DriverRequestDto dto) {
        DriverResponseDto response = driverService.save(dto);

        log.debug("New driver was added: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public DriverResponseDto update(@PathVariable @UUID String id, @RequestBody @Valid DriverRequestDto dto) {
        DriverResponseDto response = driverService.update(id, dto);

        log.debug("Driver with id {} was updated and now is: {}", id, response);
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @UUID String id) {
        driverService.delete(id);

        log.debug("Driver with id {} was removed", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public DriverResponseDto getDriverById(@PathVariable @UUID String id) {
        DriverResponseDto response = driverService.getDriverById(id);

        log.debug("Got driver by id {}: {}", id, response);
        return response;
    }

    @GetMapping("/by-email/{email}")
    public DriverResponseDto getDriverByEmail(@PathVariable @Email String email) {
        DriverResponseDto response = driverService.getDriverByEmail(email);

        log.debug("Got driver by email {}: {}", email, response);
        return response;
    }

    @GetMapping
    public ListContainerResponseDto<DriverResponseDto> getDrivers(@PageableDefault @Nullable @Valid Pageable pageable,
            @RequestParam @Nullable
            @MapContainsAllowedKeys(
                    keys = { "page", "size", "sort", "name", "email", "isAvailable", "carManufacturerName" },
                    message = REQUEST_PARAMS_VALIDATION_NOT_ALLOWED_KEYS_MESSAGE)
            @MapContainsParseableBooleanValues(
                    keys = { "isAvailable" })
            Map<String, @NotBlank String> requestParams) {

        log.debug("Get all drivers according to request parameters: {}", requestParams);
        ListContainerResponseDto<DriverResponseDto> response = driverService.getDrivers(pageable, requestParams);

        log.debug("Got all drivers. Total count: {}. Pageable settings: {}", response.values().size(), pageable);
        return response;
    }

    @GetMapping("/available")
    public ListContainerResponseDto<DriverResponseDto> getAvailableDrivers(
            @PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<DriverResponseDto> response = driverService.getAvailableDrivers(pageable);

        log.debug("Got available drivers. Total count: {}. Pageable settings: {}", response.values().size(), pageable);
        return response;
    }

    @PatchMapping("/{id}/availability")
    public DriverResponseDto updateAvailability(@PathVariable @UUID String id,
            @RequestBody
            @Valid
            @MapContainsKey(IS_AVAILABLE_KEY)
            @Size(min = 1, max = 1, message = PATCH_VALIDATION_SIZE_MESSAGE_KEY) Map<String, @NotNull(
                    message = PATCH_VALIDATION_AVAILABILITY_NOT_NULL_MESSAGE_KEY) Boolean> patch) {

        Boolean value = patch.get(IS_AVAILABLE_KEY);

        DriverResponseDto response = driverService.updateAvailability(id, value);

        log.debug("Set availability for driver with id {} to {}", id, value);
        return response;
    }
}
