package com.jet.employeeapi.domain.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(UUID uuid) {
        super("Could not find employee " + uuid.toString());
    }

}
