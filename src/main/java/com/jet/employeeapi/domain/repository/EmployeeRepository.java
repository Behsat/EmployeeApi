package com.jet.employeeapi.domain.repository;

import com.jet.employeeapi.domain.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long> {

    Employee findByUuid(UUID uuid);

    Employee findByEmail(String email);
}