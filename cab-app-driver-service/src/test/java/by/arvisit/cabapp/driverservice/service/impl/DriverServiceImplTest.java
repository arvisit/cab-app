package by.arvisit.cabapp.driverservice.service.impl;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_DRIVER_EMAIL;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_DRIVER_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.NEW_DRIVER_EMAIL;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.NEW_DRIVER_NAME;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriver;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverRequestDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverResponseDtoInListContainer;
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
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.mapper.DriverMapper;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import by.arvisit.cabapp.driverservice.persistence.repository.DriverRepository;
import by.arvisit.cabapp.driverservice.persistence.util.DriverSpecs;
import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @InjectMocks
    private DriverServiceImpl driverService;
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private DriverMapper driverMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private DriverSpecs driverSpecs;

    @Test
    void shouldReturnContainerWithDriverResponseDto_whenGetDrivers() {
        DriverResponseDto driverResponseDto = getDriverResponseDto().build();
        Driver driver = getDriver().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<Driver> drivers = List.of(driver);
        Page<Driver> driversPage = new PageImpl<>(drivers);
        Specification<Driver> spec = (root, query, cb) -> cb.conjunction();

        when(driverSpecs.getAllByFilter(any()))
                .thenReturn(spec);
        when(driverRepository.findAll(spec, pageable))
                .thenReturn(driversPage);
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);
        when(driverRepository.count(spec))
                .thenReturn((long) drivers.size());

        assertThat(driverService.getDrivers(pageable, any()))
                .isEqualTo(getDriverResponseDtoInListContainer().build());
    }

    @Test
    void shouldReturnContainerWithAvailableDriverResponseDto_whenGetAvailableDrivers() {
        DriverResponseDto driverResponseDto = getDriverResponseDto().build();
        Driver driver = getDriver().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<Driver> drivers = List.of(driver);

        when(driverRepository.findByIsAvailableTrue(pageable))
                .thenReturn(drivers);
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);
        when(driverRepository.countByIsAvailableTrue())
                .thenReturn((long) drivers.size());

        assertThat(driverService.getAvailableDrivers(pageable))
                .isEqualTo(getDriverResponseDtoInListContainer().build());

    }

    @Test
    void shouldReturnDriverResponseDto_whenGetDriverByExistingId() {
        Driver driver = getDriver().build();
        DriverResponseDto driverResponseDto = getDriverResponseDto().build();

        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(driver));
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);

        assertThat(driverService.getDriverById(DEFAULT_DRIVER_ID_STRING))
                .isEqualTo(driverResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetDriverByNonExistingId() {
        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.getDriverById(DEFAULT_DRIVER_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnDriverResponseDto_whenGetDriverByExistingEmail() {
        Driver driver = getDriver().build();
        DriverResponseDto driverResponseDto = getDriverResponseDto().build();

        when(driverRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(driver));
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);

        assertThat(driverService.getDriverByEmail(DEFAULT_DRIVER_EMAIL))
                .isEqualTo(driverResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetDriverByNonExistingEmail() {
        when(driverRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.getDriverByEmail(DEFAULT_DRIVER_EMAIL))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnDriverResponseDto_whenSaveDriverWithNotInUseEmail() {
        Driver driver = getDriver().build();
        DriverRequestDto driverRequestDto = getDriverRequestDto().build();
        DriverResponseDto driverResponseDto = getDriverResponseDto().build();

        when(driverRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(driverMapper.fromRequestDtoToEntity(any(DriverRequestDto.class)))
                .thenReturn(driver);
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);
        when(driverRepository.save(any(Driver.class)))
                .thenReturn(driver);

        assertThat(driverService.save(driverRequestDto))
                .isEqualTo(driverResponseDto);
    }

    @Test
    void shouldThrowUsernameAlreadyExistsException_whenSaveDriverWithInUseEmail() {
        DriverRequestDto driverRequestDto = getDriverRequestDto().build();

        when(driverRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> driverService.save(driverRequestDto))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void shouldReturnDriverResponseDto_whenUpdateExistingDriverWithSameEmailAndNewName() {
        DriverRequestDto driverRequestDto = getDriverRequestDto()
                .withName(NEW_DRIVER_NAME)
                .build();
        Driver existingDriver = getDriver().build();
        Driver savedDriver = getDriver()
                .withName(NEW_DRIVER_NAME)
                .build();
        DriverResponseDto driverResponseDto = getDriverResponseDto()
                .withName(NEW_DRIVER_NAME)
                .build();

        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingDriver));
        when(driverRepository.existsByEmail(anyString()))
                .thenReturn(true);
        doNothing().when(driverMapper)
                .updateEntityWithRequestDto(any(DriverRequestDto.class), any(Driver.class));
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);
        when(driverRepository.save(any(Driver.class)))
                .thenReturn(savedDriver);

        assertThat(driverService.update(DEFAULT_DRIVER_ID_STRING, driverRequestDto))
                .isEqualTo(driverResponseDto);
    }

    @Test
    void shouldReturnDriverResponseDto_whenUpdateExistingDriverWithNewNotInUseEmail() {
        DriverRequestDto driverRequestDto = getDriverRequestDto()
                .withEmail(NEW_DRIVER_EMAIL)
                .build();
        Driver existingDriver = getDriver().build();
        Driver savedDriver = getDriver()
                .withEmail(NEW_DRIVER_EMAIL)
                .build();
        DriverResponseDto driverResponseDto = getDriverResponseDto()
                .withEmail(NEW_DRIVER_EMAIL)
                .build();

        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingDriver));
        when(driverRepository.existsByEmail(anyString()))
                .thenReturn(false);
        doNothing().when(driverMapper)
                .updateEntityWithRequestDto(any(DriverRequestDto.class), any(Driver.class));
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);
        when(driverRepository.save(any(Driver.class)))
                .thenReturn(savedDriver);

        assertThat(driverService.update(DEFAULT_DRIVER_ID_STRING, driverRequestDto))
                .isEqualTo(driverResponseDto);
    }

    @Test
    void shouldThrowUsernameAlreadyExistsException_whenUpdateExistingDriverWithInUseEmailOfAnotherDriver() {
        DriverRequestDto driverRequestDto = getDriverRequestDto()
                .withEmail(NEW_DRIVER_EMAIL)
                .build();
        Driver existingDriver = getDriver().build();

        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingDriver));
        when(driverRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> driverService.update(DEFAULT_DRIVER_ID_STRING, driverRequestDto))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdateNonExistingDriver() {
        DriverRequestDto driverRequestDto = getDriverRequestDto().build();

        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.update(DEFAULT_DRIVER_ID_STRING, driverRequestDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldCallRepository_whenUpdateAvailabilityOfExistingDriverWithNewValue() {
        boolean oldValue = true;
        Driver existingDriver = getDriver()
                .withIsAvailable(oldValue)
                .build();
        boolean newValue = false;
        DriverResponseDto driverResponseDto = getDriverResponseDto()
                .withIsAvailable(newValue)
                .build();
        Driver updatedDriver = getDriver()
                .withIsAvailable(newValue)
                .build();

        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingDriver));
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);
        when(driverRepository.save(any(Driver.class)))
                .thenReturn(updatedDriver);

        DriverResponseDto actualResponseDto = driverService.updateAvailability(DEFAULT_DRIVER_ID_STRING, newValue);

        verify(driverRepository, times(1)).save(existingDriver);

        assertThat(actualResponseDto.isAvailable())
                .isEqualTo(newValue);
        assertThat(oldValue)
                .isNotEqualTo(newValue);
    }

    @Test
    void shouldNotCallRepository_whenUpdateAvailabilityOfExistingDriverWithSameValue() {
        boolean oldValue = true;
        Driver existingDriver = getDriver()
                .withIsAvailable(oldValue)
                .build();
        boolean newValue = true;
        DriverResponseDto driverResponseDto = getDriverResponseDto()
                .withIsAvailable(newValue)
                .build();

        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingDriver));
        when(driverMapper.fromEntityToResponseDto(any(Driver.class)))
                .thenReturn(driverResponseDto);

        DriverResponseDto actualResponseDto = driverService.updateAvailability(DEFAULT_DRIVER_ID_STRING, newValue);

        verify(driverRepository, times(0)).save(existingDriver);

        assertThat(actualResponseDto.isAvailable())
                .isEqualTo(newValue);
        assertThat(oldValue)
                .isEqualTo(newValue);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void shouldThrowEntityNotFoundException_whenUpdateAvailabilityOfNonExistingDriver(boolean value) {
        when(driverRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.updateAvailability(DEFAULT_DRIVER_ID_STRING, value))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowNothing_whenDeleteExistingDriver() {
        when(driverRepository.existsById(any(UUID.class)))
                .thenReturn(true);
        doNothing().when(driverRepository)
                .deleteById(any(UUID.class));

        assertThatNoException()
                .isThrownBy(() -> driverService.delete(DEFAULT_DRIVER_ID_STRING));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeleteNonExistingDriver() {
        when(driverRepository.existsById(any(UUID.class)))
                .thenReturn(false);

        assertThatThrownBy(() -> driverService.delete(DEFAULT_DRIVER_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
