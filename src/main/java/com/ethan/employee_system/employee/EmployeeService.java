package com.ethan.employee_system.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeDto> findAll() {
        return repository.findAll().stream()
                .map(EmployeeDto::from)
                .toList();
    }

    public EmployeeDto findById(Long id) {
        return repository.findById(id)
                .map(EmployeeDto::from)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional
    public EmployeeDto create(EmployeeCreateRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new EmployeeDuplicateEmailException(request.email());
        }

        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setPassword(passwordEncoder.encode(request.password()));
        employee.setPhone(request.phone());
        employee.setPosition(request.position());
        employee.setDepartmentId(request.departmentId());
        employee.setRole(request.role() != null ? request.role() : Employee.Role.EMPLOYEE);
        employee.setHireDate(request.hireDate());

        return EmployeeDto.from(repository.save(employee));
    }

    @Transactional
    public EmployeeDto update(Long id, EmployeeUpdateRequest request) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (request.firstName() != null) employee.setFirstName(request.firstName());
        if (request.lastName() != null) employee.setLastName(request.lastName());
        if (request.email() != null && !request.email().equals(employee.getEmail())) {
            if (repository.existsByEmail(request.email())) {
                throw new EmployeeDuplicateEmailException(request.email());
            }
            employee.setEmail(request.email());
        }
        if (request.password() != null && !request.password().isBlank()) {
            employee.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.phone() != null) employee.setPhone(request.phone());
        if (request.position() != null) employee.setPosition(request.position());
        if (request.departmentId() != null) employee.setDepartmentId(request.departmentId());
        if (request.role() != null) employee.setRole(request.role());
        if (request.hireDate() != null) employee.setHireDate(request.hireDate());
        if (request.isActive() != null) employee.setIsActive(request.isActive());

        return EmployeeDto.from(repository.save(employee));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
