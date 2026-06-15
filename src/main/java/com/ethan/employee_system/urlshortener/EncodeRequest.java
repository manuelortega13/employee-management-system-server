package com.ethan.employee_system.urlshortener;

import jakarta.validation.constraints.NotBlank;

public record EncodeRequest(
        @NotBlank String url
) {}
