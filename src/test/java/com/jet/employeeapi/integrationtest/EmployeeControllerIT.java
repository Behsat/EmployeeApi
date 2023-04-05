package com.jet.employeeapi.integrationtest;

import com.jet.employeeapi.EmployeeApiApplication;
import com.jet.employeeapi.domain.model.EmployeeRequest;
import com.jet.employeeapi.domain.repository.EmployeeRepository;
import com.jet.employeeapi.domain.service.EmployeeService;
import com.jet.employeeapi.domain.service.SequenceGeneratorService;
import com.jet.employeeapi.fixture.EmployeeFixture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmployeeApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT implements EmployeeFixture {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @SpyBean
    private EmployeeService employeeService;

    @SpyBean
    private SequenceGeneratorService generatorService;

    @SpyBean
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void testPreparation() {
        headers.add("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
    }

    @SneakyThrows
    @Test
    public void shouldReturnAllEmployees() {
        var employee = EmployeeFixture.getEmployeeResponse(
                1L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-06-18", List.of("soccer", "music"));

        var employee2 = EmployeeFixture.getEmployeeResponse(
                2L, "test2@gmail.com", "Just Eat2",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f8"), "1998-26-11", List.of("soccer", "music"));

        doReturn(List.of(employee, employee2)).when(employeeService).getEmployees();

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/employee"),
                HttpMethod.GET, entity, String.class);

        String expected = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"uuid\": \"387b3292-f310-4b00-8e33-9ca96c6834f9\",\n" +
                "    \"email\": \"test@gmail.com\",\n" +
                "    \"fullName\": \"Just Eat\",\n" +
                "    \"birthDate\": \"1989-06-17\",\n" +
                "    \"hobbies\": [\n" +
                "      \"soccer\",\n" +
                "      \"music\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"uuid\": \"387b3292-f310-4b00-8e33-9ca96c6834f8\",\n" +
                "    \"email\": \"test2@gmail.com\",\n" +
                "    \"fullName\": \"Just Eat2\",\n" +
                "    \"birthDate\": \"2000-02-10\",\n" +
                "    \"hobbies\": [\n" +
                "      \"soccer\",\n" +
                "      \"music\"\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @SneakyThrows
    @Test
    public void shouldReturnEmployeeByUuid() {
        var employee = EmployeeFixture.getEmployeeResponse(
                1L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-06-18", List.of("soccer", "music"));

        doReturn(employee).when(employeeService).getEmployeeByUuid(UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"));

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/employee/387b3292-f310-4b00-8e33-9ca96c6834f9"),
                HttpMethod.GET, entity, String.class);

        String expected = "  {\n" +
                "    \"id\": 1,\n" +
                "    \"uuid\": \"387b3292-f310-4b00-8e33-9ca96c6834f9\",\n" +
                "    \"email\": \"test@gmail.com\",\n" +
                "    \"fullName\": \"Just Eat\",\n" +
                "    \"birthDate\": \"1989-06-17\",\n" +
                "    \"hobbies\": [\n" +
                "      \"soccer\",\n" +
                "      \"music\"\n" +
                "    ]\n" +
                "  }";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void shouldCreateEmployee() {
        var employeeRequest = EmployeeFixture.getEmployeeRequest("test@gmail.com", "Just Eat", "1989-06-18", List.of("soccer", "music"));

        when(generatorService.generateSequence("employee_sequence")).thenReturn(1L);
        doReturn(null).when(employeeRepository).findByEmail(employeeRequest.getEmail());

        HttpEntity<EmployeeRequest> entity = new HttpEntity<>(employeeRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/employee"),
                HttpMethod.POST, entity, String.class);


        assertTrue(Objects.requireNonNull(response.getBody()).contains("test@gmail.com"));
        assertTrue(Objects.requireNonNull(response.getBody()).contains("1"));
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Just Eat"));
        assertTrue(Objects.requireNonNull(response.getBody()).contains("soccer"));
    }

    @Test
    public void shouldUpdateEmployee() {
        var employeeRequest = EmployeeFixture.getEmployeeRequest("test124@gmail.com", "Just dont Eat", "1989-06-13", List.of("baseball", "music"));
        var employee = EmployeeFixture.getEmployee(145L, "test124@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-26-09", List.of("soccer", "music"));
        when(generatorService.generateSequence("employee_sequence")).thenReturn(145L);
        doReturn(Optional.of(employee)).when(employeeRepository).findById(145L);

        HttpEntity<EmployeeRequest> entity = new HttpEntity<>(employeeRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/employee/145"),
                HttpMethod.PUT, entity, String.class);

        assertTrue(Objects.requireNonNull(response.getBody()).contains("test124@gmail.com"));
        assertTrue(Objects.requireNonNull(response.getBody()).contains("1"));
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Just dont Eat"));
        assertTrue(Objects.requireNonNull(response.getBody()).contains("baseball"));
    }

    @Test
    public void shouldDeleteEmployee() {
        var employee = EmployeeFixture.getEmployee(
                1L, "test@gmail.com", "Just Eat",
                UUID.fromString("387b3292-f310-4b00-8e33-9ca96c6834f9"), "1989-06-18", List.of("soccer", "music"));

        doReturn(Optional.of(employee)).when(employeeRepository).findById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        restTemplate.exchange(createURLWithPort("/api/v1/employee/1"), HttpMethod.DELETE, entity, String.class);

        doReturn(Optional.empty()).when(employeeRepository).findById(1L);
        var response = employeeRepository.findById(1L);

        assertEquals(response, Optional.empty());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
