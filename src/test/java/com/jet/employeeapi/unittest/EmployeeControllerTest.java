package com.jet.employeeapi.unittest;

import com.jet.employeeapi.api.resource.EmployeeController;
import com.jet.employeeapi.domain.service.EmployeeService;
import com.jet.employeeapi.fixture.EmployeeFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest implements EmployeeFixture {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private static final UUID UUID_EMPLOYEE =  UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9");
    private static final UUID UUID_EMPLOYEE_2 =  UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f8");

    @Test
    public void shouldCreateEmployee() {

        var employeeRequest = EmployeeFixture.getEmployeeRequest("test@gmail.com", "Just Eat", "1989-26-09", List.of("soccer", "music"));
        var employeeResponse = EmployeeFixture.getEmployeeResponse("test@gmail.com", "Just Eat",
                UUID_EMPLOYEE, "1989-26-09", List.of("soccer", "music"));

        when(employeeService.createEmployee(employeeRequest)).thenReturn(employeeResponse);

        var response = employeeController.createEmployee(employeeRequest);

        assertThat(response.getFullName()).isEqualTo(employeeResponse.getFullName());

        assertThat(response.getUuid()).isEqualTo(employeeResponse.getUuid());
        assertThat(response.getBirthDate()).isEqualTo(employeeResponse.getBirthDate());
        assertThat(response.getEmail()).isEqualTo(employeeResponse.getEmail());
        assertThat(response.getHobbies()).containsAll(employeeResponse.getHobbies());
    }

    @Test
    public void shouldReturnAllEmployees() {
        var employee = EmployeeFixture.getEmployeeResponse(
                 "test@gmail.com", "Just Eat",
                UUID_EMPLOYEE, "1989-06-18", List.of("soccer", "music"));

        var employee2 = EmployeeFixture.getEmployeeResponse("test2@gmail.com", "Just Eat2",
                UUID_EMPLOYEE_2, "1998-26-11", List.of("soccer", "music"));

        when(employeeService.getEmployees()).thenReturn(List.of(employee, employee2));

        var response = employeeController.getEmployees();

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getFullName()).isEqualTo(employee.getFullName());

        assertThat(response.get(0).getUuid()).isEqualTo(employee.getUuid());
        assertThat(response.get(1).getBirthDate()).isEqualTo(employee2.getBirthDate());
        assertThat(response.get(1).getEmail()).isEqualTo(employee2.getEmail());
        assertThat(response.get(1).getHobbies()).containsAll(employee2.getHobbies());
    }

    @Test
    public void shouldReturnEmployeeByUuid() {
        var employeeResponse = EmployeeFixture.getEmployeeResponse(
                 "test@gmail.com", "Just Eat",
                UUID_EMPLOYEE, "1989-06-18", List.of("soccer", "music"));

        when(employeeService.getEmployeeByUuid(UUID_EMPLOYEE)).thenReturn(employeeResponse);

        var response = employeeController.getEmployeeByUuid(UUID_EMPLOYEE);

        assertThat(response.getFullName()).isEqualTo(employeeResponse.getFullName());
        assertThat(response.getUuid()).isEqualTo(employeeResponse.getUuid());
        assertThat(response.getBirthDate()).isEqualTo(employeeResponse.getBirthDate());
        assertThat(response.getEmail()).isEqualTo(employeeResponse.getEmail());
        assertThat(response.getHobbies()).containsAll(employeeResponse.getHobbies());
    }

    @Test
    public void shouldUpdateEmployee() {
        var employeeRequest = EmployeeFixture.getEmployeeRequest("test@gmail.com", "Just Eat", "1989-26-09", List.of("soccer", "music"));
        var employeeResponse = EmployeeFixture.getEmployeeResponse(
                 "changed@gmail.com", "Never sleep",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca45c6834f9"), "1989-06-18", List.of("basketball", "guitar"));

        when(employeeService.updateEmployee(employeeRequest, UUID.fromString("387b3292-f310-4b00-8e33-9ca45c6834f9"))).thenReturn(employeeResponse);

        var response = employeeController.updateEmployee(employeeRequest, UUID.fromString("387b3292-f310-4b00-8e33-9ca45c6834f9"));

        assertThat(response.getFullName()).isEqualTo(employeeResponse.getFullName());
        assertThat(response.getUuid()).isEqualTo(employeeResponse.getUuid());
        assertThat(response.getBirthDate()).isEqualTo(employeeResponse.getBirthDate());
        assertThat(response.getEmail()).isEqualTo(employeeResponse.getEmail());
        assertThat(response.getHobbies()).containsAll(employeeResponse.getHobbies());
    }
}
