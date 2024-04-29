package by.arvisit.cabapp.passengerservice.service.impl;

import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_CARD_NUMBER;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_EMAIL;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_NAME;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassenger;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerRequestDto;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.mapper.PassengerMapper;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import by.arvisit.cabapp.passengerservice.persistence.repository.PassengerRepository;
import by.arvisit.cabapp.passengerservice.persistence.util.PassengerSpecs;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
public class PassengerServiceImplComponentTest {

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

    private PassengerRequestDto newPassengerToSave;
    private PassengerResponseDto newSavedPassenger;
    private PassengerRequestDto passengerToUpdate;
    private PassengerResponseDto updatedPassenger;
    private String passengerToDeleteId;
    private String idToFindPassenger;
    private PassengerResponseDto passengerFoundById;
    private String emailToFindPassenger;
    private PassengerResponseDto passengerFoundByEmail;
    private Integer pageSize;
    private Integer passengersCount;
    private ListContainerResponseDto<PassengerResponseDto> allPassengersResponse;

    @Given("I want to save a new passenger with name {string}, email {string} and card number {string}")
    public void prepareNewPassengerForSaving(String name, String email, String cardNumber) {
        newPassengerToSave = getPassengerRequestDto()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("I perform saving")
    public void saveNewPassengerActually() {
        Passenger passenger = getPassenger().build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto().build();

        when(passengerRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(passengerMapper.fromRequestDtoToEntity(any(PassengerRequestDto.class)))
                .thenReturn(passenger);
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);
        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(passenger);

        newSavedPassenger = passengerService.save(newPassengerToSave);
    }

    @Then("a new passenger should be saved")
    public void checkIfNewPassengerSaved() {
        assertNotNull(newSavedPassenger);
    }

    @Then("have name {string}, email {string} and card number {string}")
    public void checkIfNewPassengerHasSpecifiedState(String name, String email, String cardNumber) {
        assertThat(newSavedPassenger.name())
                .isEqualTo(name);
        assertThat(newSavedPassenger.email())
                .isEqualTo(email);
        assertThat(newSavedPassenger.cardNumber())
                .isEqualTo(cardNumber);
    }

    @Then("also have an id")
    public void checkIfNewPassengerHasId() {
        assertNotNull(newSavedPassenger.id());
    }

    @Given("I want to update an existing passenger with new name {string}, email {string} and card number {string} values")
    public void preparePassengerToUpdate(String name, String email, String cardNumber) {
        passengerToUpdate = getPassengerRequestDto()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("I perform update of existing passenger with id {string}")
    public void updateExistingPassenger(String id) {
        Passenger existingPassenger = getPassenger().build();
        Passenger savedPassenger = getPassenger()
                .withName(NEW_NAME)
                .withEmail(NEW_EMAIL)
                .withCardNumber(NEW_CARD_NUMBER)
                .build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto()
                .withName(NEW_NAME)
                .withEmail(NEW_EMAIL)
                .withCardNumber(NEW_CARD_NUMBER)
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

        updatedPassenger = passengerService.update(id, passengerToUpdate);
    }

    @Then("changes should be saved")
    public void checkIfPassengerWasUpdated() {
        assertNotNull(updatedPassenger);
    }

    @Then("I should receive response for passenger with id {string} and applied name {string}, email {string} and card number {string}")
    public void checkIfPassengerWasUpdatedWithCorrectValues(String id, String name, String email, String cardNumber) {
        assertThat(updatedPassenger.id())
                .isEqualTo(id);
        assertThat(updatedPassenger.name())
                .isEqualTo(name);
        assertThat(updatedPassenger.email())
                .isEqualTo(email);
        assertThat(updatedPassenger.cardNumber())
                .isEqualTo(cardNumber);
    }

    @Given("I want to delete an existing passenger with id {string}")
    public void checkIfPassengerToDeleteExists(String id) {
        passengerToDeleteId = id;
        when(passengerRepository.existsById(any(UUID.class)))
                .thenReturn(true);

        UUID uuid = UUID.fromString(id);
        assertTrue(passengerRepository.existsById(uuid));
    }

    @When("I delete a passenger")
    public void deleteExistingPassenger() {
        when(passengerRepository.existsById(any(UUID.class)))
                .thenReturn(true);
        doNothing().when(passengerRepository)
                .deleteById(any(UUID.class));

        assertThatNoException()
                .isThrownBy(() -> passengerService.delete(passengerToDeleteId));
    }

    @Then("passenger with id {string} should be deleted")
    public void checkIfPassengerWasDeleted(String id) {
        when(passengerRepository.existsById(any(UUID.class)))
                .thenReturn(false);

        UUID uuid = UUID.fromString(id);
        assertFalse(passengerRepository.existsById(uuid));
    }

    @Given("I want to get details about existing passenger with id {string}")
    public void checkIfPassengerToFindByIdExists(String id) {
        idToFindPassenger = id;
        when(passengerRepository.existsById(any(UUID.class)))
                .thenReturn(true);

        UUID uuid = UUID.fromString(id);
        assertTrue(passengerRepository.existsById(uuid));
    }

    @When("I search for the passenger with this id")
    public void getPassengerById() {
        Passenger passenger = getPassenger().build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto().build();

        when(passengerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(passenger));
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);

        passengerFoundById = passengerService.getPassengerById(idToFindPassenger);
    }

    @Then("I should receive a response with details for passenger with id {string}")
    public void checkIfPassengerWasFoundById(String id) {
        assertNotNull(passengerFoundById);
        assertThat(passengerFoundById.id())
                .isEqualTo(id);
    }

    @Given("I want to get details about existing passenger with email {string}")
    public void checkIfPassengerToFindByEmailExists(String email) {
        emailToFindPassenger = email;
        when(passengerRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertTrue(passengerRepository.existsByEmail(email));
    }

    @When("I search for the passenger with this email")
    public void getPassengerByEmail() {
        Passenger passenger = getPassenger().build();
        PassengerResponseDto passengerResponseDto = getPassengerResponseDto().build();

        when(passengerRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(passenger));
        when(passengerMapper.fromEntityToResponseDto(any(Passenger.class)))
                .thenReturn(passengerResponseDto);

        passengerFoundByEmail = passengerService.getPassengerByEmail(emailToFindPassenger);
    }

    @Then("I should receive a response with details for passenger with email {string}")
    public void checkIfPassengerWasFoundByEmail(String email) {
        assertNotNull(passengerFoundByEmail);
        assertThat(passengerFoundByEmail.email())
                .isEqualTo(email);
    }

    @Given("there are two existing passengers")
    public void prepareAllPassengers(DataTable table) {
        List<Passenger> passengers = new ArrayList<>();
        List<PassengerResponseDto> passengerResponseDtos = new ArrayList<>();
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> columns : rows) {
            String id = columns.get(0);
            String name = columns.get(1);
            String email = columns.get(2);
            String cardNumber = columns.get(3);
            passengers.add(Passenger.builder()
                    .withId(UUID.fromString(id))
                    .withName(name)
                    .withEmail(email)
                    .withCardNumber(cardNumber)
                    .build());
            passengerResponseDtos.add(PassengerResponseDto.builder()
                    .withId(id)
                    .withName(name)
                    .withEmail(email)
                    .withCardNumber(cardNumber)
                    .build());
        }
        Page<Passenger> passengersPage = new PageImpl<>(passengers);
        Specification<Passenger> spec = (root, query, cb) -> cb.conjunction();

        when(passengerSpecs.getAllByFilter(any()))
                .thenReturn(spec);
        when(passengerRepository.findAll(eq(spec), any(Pageable.class)))
                .thenReturn(passengersPage);

        passengersCount = passengers.size();
        for (int i = 0; i < passengersCount; i++) {
            when(passengerMapper.fromEntityToResponseDto(passengers.get(i)))
                    .thenReturn(passengerResponseDtos.get(i));
        }
        when(passengerRepository.count(spec))
                .thenReturn((long) passengersCount);
    }

    @When("I try to get all passengers from the first page of size {int} without sorting")
    public void getAllPassengers(Integer size) {
        pageSize = size;
        Pageable pageable = Pageable.ofSize(size);
        allPassengersResponse = passengerService.getPassengers(pageable, any());
    }

    @Then("I should receive a response with pageable details and list of all passengers")
    public void confirmGetAllPassengers() {
        assertThat(allPassengersResponse.currentPage())
                .isZero();
        assertThat(allPassengersResponse.lastPage())
                .isZero();
        assertThat(allPassengersResponse.size())
                .isEqualTo(pageSize);
        assertThat(allPassengersResponse.values()).size()
                .isEqualTo(passengersCount);

    }
}
