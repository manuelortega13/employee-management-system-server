package com.ethan.employee_system.employee;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String position,
        Long departmentId,
        Employee.Role role,
        LocalDate hireDate,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EmployeeDto from(Employee entity) {
        return new EmployeeDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPosition(),
                entity.getDepartmentId(),
                entity.getRole(),
                entity.getHireDate(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
