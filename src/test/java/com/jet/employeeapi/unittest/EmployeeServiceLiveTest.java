package com.jet.employeeapi.unittest;

import com.jet.employeeapi.api.mapper.EmployeeMapper;
import com.jet.employeeapi.domain.exception.EmployeeEmailExistsException;
import com.jet.employeeapi.domain.exception.EmployeeNotFoundException;
import com.jet.employeeapi.domain.repository.EmployeeRepository;
import com.jet.employeeapi.domain.service.EmployeeServiceLive;
import com.jet.employeeapi.domain.service.SequenceGeneratorService;
import com.jet.employeeapi.fixture.EmployeeFixture;
import com.jet.employeeapi.infrastructure.kafka.KafkaProducerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceLiveTest implements EmployeeFixture {

    @InjectMocks
    private EmployeeServiceLive employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SequenceGeneratorService generatorService;

    @Mock
    private KafkaProducerService producerService;

    @Spy
    EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @Test
    @SneakyThrows
    public void shouldCreateEmployeeSuccessfully() {

        when(generatorService.generateSequence("employee_sequence")).thenReturn(1L);

        var employeeRequest = EmployeeFixture.getEmployeeRequest("test@gmail.com", "Just Eat", "1989-26-09", List.of("soccer", "music"));
        var employee = EmployeeFixture.getEmployee(generatorService.generateSequence("employee_sequence"), "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-26-09", List.of("soccer", "music"));
        var employeeResponse = EmployeeFixture.getEmployeeResponse(
                generatorService.generateSequence("employee_sequence"), "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-26-09", List.of("soccer", "music"));

        when(employeeRepository.findByEmail("test@gmail.com")).thenReturn(null);
        doReturn(employee).when(employeeMapper).fromEmployeeRequestToEmployee(employeeRequest);
        doReturn(employee).when(employeeRepository).save(employee);

        var response = employeeService.createEmployee(employeeRequest);

        assertThat(response.getFullName()).isEqualTo(employeeResponse.getFullName());
        assertThat(response.getId()).isEqualTo(employeeResponse.getId());
        assertThat(response.getUuid()).isEqualTo(employeeResponse.getUuid());
        assertThat(response.getBirthDate()).isEqualTo(employeeResponse.getBirthDate());
        assertThat(response.getEmail()).isEqualTo(employeeResponse.getEmail());
        assertThat(response.getHobbies()).containsAll(employeeResponse.getHobbies());
        verify(producerService, times(1)).sendMessage(anyString());
    }

    @Test
    @SneakyThrows
    public void shouldFailWhenCreatingEmployee() {

        when(generatorService.generateSequence("employee_sequence")).thenReturn(1L);

        var employeeRequest = EmployeeFixture.getEmployeeRequest("test@gmail.com", "Just Eat", "1989-26-09", List.of("soccer", "music"));
        var employee = EmployeeFixture.getEmployee(generatorService.generateSequence("employee_sequence"), "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-26-09", List.of("soccer", "music"));

        when(employeeRepository.findByEmail("test@gmail.com")).thenReturn(employee);

        var errorMessage = "This email is being used by someone else: test@gmail.com";

        assertThatThrownBy(() -> employeeService.createEmployee(employeeRequest)).isInstanceOf(EmployeeEmailExistsException.class).hasMessage(errorMessage);
    }

    @Test
    public void shouldReturnAllEmployees() {

        var employee = EmployeeFixture.getEmployee(
                1L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-06-18", List.of("soccer", "music"));

        var employee2 = EmployeeFixture.getEmployee(
                2L, "test2@gmail.com", "Just Eat2",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f8"), "1998-26-11", List.of("soccer", "music"));

        doReturn(List.of(employee, employee2)).when(employeeRepository).findAll();

        var result = employeeService.getEmployees();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnAllEmployeeByUuid() {

        var employee = EmployeeFixture.getEmployee(
                1L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-06-18", List.of("soccer", "music"));

        doReturn(employee).when(employeeRepository).findByUuid(UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"));

        var response = employeeService.getEmployeeByUuid(UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"));

        assertThat(response.getFullName()).isEqualTo(employee.getFullName());
        assertThat(response.getId()).isEqualTo(employee.getId());
        assertThat(response.getUuid()).isEqualTo(employee.getUuid());
        assertThat(response.getBirthDate()).isEqualTo(employee.getBirthDate());
        assertThat(response.getEmail()).isEqualTo(employee.getEmail());
        assertThat(response.getHobbies()).containsAll(employee.getHobbies());
    }

    @Test
    public void shouldFailWhenUpdatingEmployee() {

        var employeeRequest = EmployeeFixture.getEmployeeRequest("test467@gmail.com", "Just Eat", "1989-26-09", List.of("soccer", "music"));
        var employee = EmployeeFixture.getEmployee(26L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-26-09", List.of("soccer", "music"));
        var employee2 = EmployeeFixture.getEmployee(26L, "test467@gmail.com", "Just Bla Eat",
                UUID.fromString("387b3292-f310-4b00-3e33-9ca96c6854f9"), "1989-26-09", List.of("baseball", "music"));
        var errorMessage = "This email is being used by someone else: test467@gmail.com";

        when(employeeRepository.findById(26L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail("test467@gmail.com")).thenReturn(employee2);

        assertThatThrownBy(() -> employeeService.updateEmployee(employeeRequest, 26L)).isInstanceOf(EmployeeEmailExistsException.class).hasMessage(errorMessage);
    }

    @Test
    public void shouldFailWhenNotExistedEmployee() {
        var employeeRequest = EmployeeFixture.getEmployeeRequest("test@gmail.com", "Just Eat", "1989-26-09", List.of("soccer", "music"));
        var errorMessage = "Could not find employee 56";
        when(employeeRepository.findById(56L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(employeeRequest, 56L)).isInstanceOf(EmployeeNotFoundException.class).hasMessage(errorMessage);
    }

    @Test
    public void shouldSuccessfullyUpdateEmployee() {
        var employeeRequest = EmployeeFixture.getEmployeeRequest("test@gmail.com", "Lars Ken", "1989-26-09", List.of("soccer", "music"));
        var employee = EmployeeFixture.getEmployee(1L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-26-09", List.of("soccer", "music"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));
        doReturn(employee).when(employeeRepository).save(employee);

        var response = employeeService.updateEmployee(employeeRequest, 1L);

        assertThat(response.getFullName()).isNotEqualTo("Just Eat");
        assertThat(response.getFullName()).isEqualTo("Lars Ken");
        verify(producerService, times(1)).sendMessage(anyString());
    }

    @Test
    public void shouldDeleteCampaign() {
        var employee = EmployeeFixture.getEmployee(1L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-26-09", List.of("soccer", "music"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);
        verify(producerService, times(1)).sendMessage(anyString());
    }

    @Test
    public void shouldFailWhenDeletingCampaign() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        employeeService.deleteEmployee(1L);
        verifyNoInteractions(producerService);
    }
}

