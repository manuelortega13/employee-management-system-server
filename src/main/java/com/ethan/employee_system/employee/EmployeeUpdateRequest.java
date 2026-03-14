package com.ethan.employee_system.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Email @Size(max = 255) String email,
        @Size(max = 255) String password,
        @Size(max = 30) String phone,
        @Size(max = 100) String position,
        Long departmentId,
        Employee.Role role,
        LocalDate hireDate,
        Boolean isActive
) {}
