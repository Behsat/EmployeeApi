package com.jet.employeeapi.domain.exception;

public class EmployeeEmailExistsException extends RuntimeException {

    public EmployeeEmailExistsException(String email) {
        super("This email is being used by someone else: " + email);
    }
}
