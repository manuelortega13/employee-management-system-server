package com.ethan.employee_system.urlshortener;

public record EncodeResponse(
        String originalUrl,
        String shortUrl
) {}
