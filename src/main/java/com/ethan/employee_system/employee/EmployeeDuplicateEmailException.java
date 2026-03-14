package com.ethan.employee_system.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmployeeDuplicateEmailException extends ResponseStatusException {

    public EmployeeDuplicateEmailException(String email) {
        super(HttpStatus.CONFLICT, "Employee with email '" + email + "' already exists");
    }
}
