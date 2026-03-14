package com.ethan.employee_system.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckInRequest(
        @NotNull Long employeeId,
        @NotBlank String checkInPhoto
) {}
