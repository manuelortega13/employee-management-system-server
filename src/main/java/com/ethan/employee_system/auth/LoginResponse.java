package com.ethan.employee_system.auth;

public record LoginResponse(
        String token,
        AuthUser user
) {
    public record AuthUser(
            Long id,
            String firstName,
            String lastName,
            String email,
            String role
    ) {}
}
