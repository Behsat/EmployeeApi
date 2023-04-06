package com.jet.employeeapi.api.resource;

import com.jet.employeeapi.domain.model.EmployeeRequest;
import com.jet.employeeapi.domain.model.EmployeeResponse;
import com.jet.employeeapi.domain.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Employee Service", description = "The Employee Service handling all employee operations")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @Operation(summary = "Return all Employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/employee")
    public List<EmployeeResponse> getEmployees() {
        return service.getEmployees();
    }

    @Operation(summary = "Return Employee by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/employee/{uuid}")
    public EmployeeResponse getEmployeeByUuid(@PathVariable UUID uuid) {
        return service.getEmployeeByUuid(uuid);
    }

    @Operation(summary = "Create Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Employee Email Already Exists",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/employee")
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest employee) {
        return service.createEmployee(employee);
    }

    @Operation(summary = "Update Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Employee Email Already Exists or Employee Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PutMapping("/employee/{uuid}")
    public EmployeeResponse updateEmployee(@RequestBody EmployeeRequest employee, @PathVariable UUID uuid) {
        return service.updateEmployee(employee, uuid);
    }

    @Operation(summary = "Delete Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/employee/{uuid}")
    public void deleteEmployee(@PathVariable UUID uuid) {
        service.deleteEmployee(uuid);
    }

}