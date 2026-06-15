package com.ethan.employee_system.urlshortener;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping("/encode")
    public EncodeResponse encode(@Valid @RequestBody EncodeRequest request) {
        return service.encode(request);
    }

    @PostMapping("/decode")
    public DecodeResponse decode(@Valid @RequestBody DecodeRequest request) {
        return service.decode(request);
    }

    /**
     * Resolves a short code and issues a 302 redirect to the original URL.
     */
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(service.resolveOriginalUrl(code)))
                .build();
    }
}
