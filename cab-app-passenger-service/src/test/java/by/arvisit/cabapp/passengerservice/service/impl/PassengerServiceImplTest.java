package by.arvisit.cabapp.passengerservice.service.impl;

import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.DEFAULT_EMAIL;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.DEFAULT_ID_STRING;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_EMAIL;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_NAME;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getEmptyPassengersFilterParams;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassenger;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerRequestDto;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerResponseDto;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.mapper.PassengerMapper;
import by.arvisit.cabapp.passengerservice.mapper.PassengersFilterParamsMapper;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import by.arvisit.cabapp.passengerservice.persistence.repository.PassengerRepository;
import by.arvisit.cabapp.passengerservice.persistence.util.PassengerSpecs;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @InjectMocks
    private PassengerServiceImpl passengerService;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private PassengerMapper passengerMapper;
    @Mock
    private MessageSource messageSource;
    @Mock
    private PassengerSpecs passengerSpecs;
    @Mock
    private PassengersFilterParamsMapper filterParamsMapper;
    @Mock
    private KeycloakService keycloakService;

    @Test
    void shouldReturnContainerWithPassengerResponseDto_whenGetPassengers() {
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto().build();
        Passenger passenger = getPassenger().build();

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        List<Passenger> passengers = List.of(passenger);
        Page<Passenger> passengersPage = new PageImpl<>(passengers);
        Specification<Passenger> spec = (root, query, cb) -> cb.conjunction();

        when(filterParamsMapper.fromMapParams(any()))
                .thenReturn(getEmptyPassengersFilterParams().build());
        when(passengerSpecs.getAllByFilter(any()))
                .thenReturn(spec);
        when(passengerRepository.findAll(spec, pageable))
                .thenReturn(passengersPage);
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);
        when(passengerRepository.count(spec))
                .thenReturn(1L);

        assertThat(passengerService.getPassengers(pageable, any()))
                .isEqualTo(getPassengerResponseDtoInListContainer().build());
    }

    @Test
    void shouldReturnPassengerResponseDto_whenGetPassengerByExistingId() {
        Passenger passenger = getPassenger().build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto().build();

        when(passengerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(passenger));
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);

        assertThat(passengerService.getPassengerById(DEFAULT_ID_STRING))
                .isEqualTo(passengerResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetPassengerByNonExistingId() {
        when(passengerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerService.getPassengerById(DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnPassengerResponseDto_whenGetPassengerByExistingEmail() {
        Passenger passenger = getPassenger().build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto().build();

        when(passengerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(passenger));
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);

        assertThat(passengerService.getPassengerByEmail(DEFAULT_EMAIL))
                .isEqualTo(passengerResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetPassengerByNonExistingEmail() {
        when(passengerRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerService.getPassengerByEmail(DEFAULT_EMAIL))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnPassengerResponseDto_whenSavePassengerWithNotInUseEmail() {
        Passenger passenger = getPassenger().build();
        PassengerRequestDto passengerRequestDto = getPassengerRequestDto().build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto().build();

        when(passengerRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(passengerMapper.fromRequestDtoToEntity(any(PassengerRequestDto.class)))
                .thenReturn(passenger);
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(passenger);
        when(keycloakService.addUser(any(PassengerRequestDto.class)))
                .thenReturn(DEFAULT_ID_STRING);

        assertThat(passengerService.save(passengerRequestDto))
                .isEqualTo(passengerResponseDto);
    }

    @Test
    void shouldThrowUsernameAlreadyExistsException_whenSavePassengerWithInUseEmail() {
        PassengerRequestDto passengerRequestDto = getPassengerRequestDto().build();

        when(passengerRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> passengerService.save(passengerRequestDto))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void shouldReturnPassengerResponseDto_whenUpdateExistingPassengerWithSameEmailAndNewName() {
        PassengerRequestDto passengerRequestDto = getPassengerRequestDto()
                .withName(NEW_NAME)
                .build();
        Passenger existingPassenger = getPassenger().build();
        Passenger savedPassenger = getPassenger()
                .withName(NEW_NAME)
                .build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto()
                .withName(NEW_NAME)
                .build();

        when(passengerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.existsByEmail(anyString()))
                .thenReturn(true);
        doNothing().when(passengerMapper)
                .updateEntityWithRequestDto(any(PassengerRequestDto.class), any(Passenger.class));
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(savedPassenger);

        assertThat(passengerService.update(DEFAULT_ID_STRING, passengerRequestDto))
                .isEqualTo(passengerResponseDto);
    }

    @Test
    void shouldReturnPassengerResponseDto_whenUpdateExistingPassengerWithNewNotInUseEmail() {
        PassengerRequestDto passengerRequestDto = getPassengerRequestDto()
                .withEmail(NEW_EMAIL)
                .build();
        Passenger existingPassenger = getPassenger().build();
        Passenger savedPassenger = getPassenger()
                .withEmail(NEW_EMAIL)
                .build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto()
                .withEmail(NEW_EMAIL)
                .build();

        when(passengerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.existsByEmail(anyString()))
                .thenReturn(false);
        doNothing().when(passengerMapper)
                .updateEntityWithRequestDto(any(PassengerRequestDto.class), any(Passenger.class));
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(savedPassenger);

        assertThat(passengerService.update(DEFAULT_ID_STRING, passengerRequestDto))
                .isEqualTo(passengerResponseDto);
    }

    @Test
    void shouldThrowUsernameAlreadyExistsException_whenUpdateExistingPassengerWithInUseEmailOfAnotherPassenger() {
        PassengerRequestDto passengerRequestDto = getPassengerRequestDto()
                .withEmail(NEW_EMAIL)
                .build();
        Passenger existingPassenger = getPassenger().build();

        when(passengerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingPassenger));
        when(passengerRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> passengerService.update(DEFAULT_ID_STRING, passengerRequestDto))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUpdateNonExistingPassenger() {
        PassengerRequestDto passengerRequestDto = getPassengerRequestDto().build();

        when(passengerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerService.update(DEFAULT_ID_STRING, passengerRequestDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowNothing_whenDeleteExistingPassenger() {
        when(passengerRepository.existsById(any(UUID.class)))
                .thenReturn(true);
        doNothing().when(passengerRepository)
                .deleteById(any(UUID.class));

        assertThatNoException()
                .isThrownBy(() -> passengerService.delete(DEFAULT_ID_STRING));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenDeleteExistingPassenger() {
        when(passengerRepository.existsById(any(UUID.class)))
                .thenReturn(false);

        assertThatThrownBy(() -> passengerService.delete(DEFAULT_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
