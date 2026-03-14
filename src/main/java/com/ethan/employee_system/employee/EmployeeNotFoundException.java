package com.ethan.employee_system.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmployeeNotFoundException extends ResponseStatusException {

    public EmployeeNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
    }
}
