package com.ethan.employee_system.urlshortener;

public record DecodeResponse(
        String shortUrl,
        String originalUrl
) {}
