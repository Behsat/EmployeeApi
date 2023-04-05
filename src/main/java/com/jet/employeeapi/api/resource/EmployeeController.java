package com.jet.employeeapi.api.resource;

import com.jet.employeeapi.domain.model.EmployeeRequest;
import com.jet.employeeapi.domain.model.EmployeeResponse;
import com.jet.employeeapi.domain.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @GetMapping("/employee")
    public List<EmployeeResponse> getEmployees() {
        return service.getEmployees();
    }

    @GetMapping("/employee/{uuid}")
    public EmployeeResponse getEmployeeByUuid(@PathVariable UUID uuid) {
        return service.getEmployeeByUuid(uuid);
    }

    @PostMapping("/employee")
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest employee) {
        return service.createEmployee(employee);
    }

    @PutMapping("/employee/{uuid}")
    public EmployeeResponse updateEmployee(@RequestBody EmployeeRequest employee, @PathVariable UUID uuid) {
        return service.updateEmployee(employee, uuid);
    }

    @DeleteMapping("/employee/{uuid}")
    public void deleteEmployee(@PathVariable  UUID uuid) {
        service.deleteEmployee(uuid);
    }

}