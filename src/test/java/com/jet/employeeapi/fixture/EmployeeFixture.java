package com.jet.employeeapi.fixture;

import com.jet.employeeapi.domain.model.Employee;
import com.jet.employeeapi.domain.model.EmployeeRequest;
import com.jet.employeeapi.domain.model.EmployeeResponse;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EmployeeFixture {

    @SneakyThrows
    static Employee getEmployee(
            Long id,
            String email,
            String fullName,
            UUID uuid,
            String birthDate,
            List<String> hobbies
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(birthDate);
        return Employee.builder()
                .id(id)
                .uuid(uuid)
                .email(email)
                .fullName(fullName)
                .birthDate(date)
                .hobbies(hobbies)
                .build();
    }

    @SneakyThrows
    static EmployeeResponse getEmployeeResponse(
            Long id,
            String email,
            String fullName,
            UUID uuid,
            String birthDate,
            List<String> hobbies
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(birthDate);
        return EmployeeResponse.builder()
                .id(id)
                .uuid(uuid)
                .email(email)
                .fullName(fullName)
                .birthDate(date)
                .hobbies(hobbies)
                .build();
    }

    @SneakyThrows
    static EmployeeRequest getEmployeeRequest(
            String email,
            String fullName,
            String birthDate,
            List<String> hobbies
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(birthDate);
        return EmployeeRequest.builder()
                .email(email)
                .birthDate(date)
                .fullName(fullName)
                .hobbies(hobbies)
                .build();
    }
}
