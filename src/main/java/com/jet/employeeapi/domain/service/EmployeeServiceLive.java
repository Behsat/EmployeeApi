package com.jet.employeeapi.domain.service;

import com.jet.employeeapi.api.mapper.EmployeeMapper;
import com.jet.employeeapi.domain.exception.EmployeeEmailExistsException;
import com.jet.employeeapi.domain.exception.EmployeeNotFoundException;
import com.jet.employeeapi.domain.model.Employee;
import com.jet.employeeapi.domain.model.EmployeeRequest;
import com.jet.employeeapi.domain.model.EmployeeResponse;
import com.jet.employeeapi.domain.repository.EmployeeRepository;
import com.jet.employeeapi.infrastructure.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceLive implements EmployeeService {

    private final EmployeeRepository repository;
    private final KafkaProducerService producerService;
    private final EmployeeMapper mapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (checkEmailExists(request.getEmail())) {
            throw new EmployeeEmailExistsException(request.getEmail());
        }
        var employee = mapper.fromEmployeeRequestToEmployee(request);
        var response = mapper.fromEmployeeToEmployeeResponse(repository.save(employee));

        producerService.sendMessage(String.format("Employee with %s created", response.getEmail()));
        return response;
    }

    @Override
    public List<EmployeeResponse> getEmployees() {
        var mapper = Mappers.getMapper(EmployeeMapper.class);
        return repository.findAll().stream()
                .map(mapper::fromEmployeeToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeByUuid(UUID uuid) {
        var mapper = Mappers.getMapper(EmployeeMapper.class);
        var employee = repository.findByUuid(uuid);
        return mapper.fromEmployeeToEmployeeResponse(employee.get());
    }

    @Override
    public EmployeeResponse updateEmployee(EmployeeRequest request, UUID uuid) {
        var mapper = Mappers.getMapper(EmployeeMapper.class);

        return repository.findByUuid(uuid)
                .map(employee -> {
                    if (!employee.getEmail().equals(request.getEmail()) && checkEmailExists(request.getEmail())) {
                        throw new EmployeeEmailExistsException(request.getEmail());
                    }
                    employee.setEmail(request.getEmail());
                    employee.setBirthDate(request.getBirthDate());
                    employee.setFullName(request.getFullName());
                    employee.setHobbies(request.getHobbies());
                    var response = mapper.fromEmployeeToEmployeeResponse(repository.save(employee));
                    producerService.sendMessage(String.format("Employee with %s updated", response.getEmail()));
                    return response;
                })
                .orElseThrow(() -> new EmployeeNotFoundException(uuid));
    }

    @Override
    public void deleteEmployee(UUID uuid) {
        var maybeEmployee = repository.findByUuid(uuid);

        maybeEmployee.ifPresent(employee -> {
            repository.deleteByUuid(uuid);
            producerService.sendMessage(String.format("Employee with %s deleted", uuid));
        });
    }

    private boolean checkEmailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }
}
