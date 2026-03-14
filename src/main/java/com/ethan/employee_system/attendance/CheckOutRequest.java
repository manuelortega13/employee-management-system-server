package com.ethan.employee_system.attendance;

import jakarta.validation.constraints.NotBlank;

public record CheckOutRequest(
        @NotBlank String checkOutPhoto
) {}
