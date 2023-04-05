package com.jet.employeeapi.domain.service;

import com.jet.employeeapi.domain.model.EmployeeRequest;
import com.jet.employeeapi.domain.model.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    List<EmployeeResponse> getEmployees();

    EmployeeResponse getEmployeeByUuid(UUID uuid);

    EmployeeResponse updateEmployee(EmployeeRequest request, UUID uuid);

    void deleteEmployee(UUID uuid);
}
