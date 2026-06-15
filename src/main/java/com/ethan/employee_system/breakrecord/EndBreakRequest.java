package com.ethan.employee_system.breakrecord;

import jakarta.validation.constraints.NotBlank;

public record EndBreakRequest(
        @NotBlank String endPhoto
) {}
