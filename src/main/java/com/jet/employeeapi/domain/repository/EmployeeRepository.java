package com.jet.employeeapi.domain.repository;

import com.jet.employeeapi.domain.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long> {

    Optional<Employee> findByUuid(UUID uuid);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> deleteByUuid(UUID uuid);
}