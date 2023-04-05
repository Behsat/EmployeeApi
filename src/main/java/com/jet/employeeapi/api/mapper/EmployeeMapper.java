package com.jet.employeeapi.api.mapper;

import com.jet.employeeapi.domain.model.Employee;
import com.jet.employeeapi.domain.model.EmployeeRequest;
import com.jet.employeeapi.domain.model.EmployeeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee fromEmployeeRequestToEmployee(EmployeeRequest employeeRequest);

    EmployeeResponse fromEmployeeToEmployeeResponse(Employee employee);

}
