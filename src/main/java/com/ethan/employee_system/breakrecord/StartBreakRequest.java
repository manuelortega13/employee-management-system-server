package com.ethan.employee_system.breakrecord;

import jakarta.validation.constraints.NotBlank;

public record StartBreakRequest(
        @NotBlank String startPhoto
) {}
