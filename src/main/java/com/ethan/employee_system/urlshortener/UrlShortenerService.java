package com.ethan.employee_system.urlshortener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory URL shortener. Mappings live in process memory only and are lost on
 * restart (and not shared across instances). The short code is Base62 of a
 * monotonic id, so it is reversible without storing the code itself.
 */
@Service
public class UrlShortenerService {

    private final Map<Long, String> idToUrl = new ConcurrentHashMap<>();
    private final Map<String, Long> urlToId = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Value("${urlshortener.base-domain:https://my.pass}")
    private String baseDomain;

    public EncodeResponse encode(EncodeRequest request) {
        String originalUrl = request.url().trim();
        validateUrl(originalUrl);

        // Atomically reuse the existing id for a known URL, or assign a new one.
        long id = urlToId.computeIfAbsent(originalUrl, url -> {
            long newId = sequence.incrementAndGet();
            idToUrl.put(newId, url);
            return newId;
        });

        return new EncodeResponse(originalUrl, toShortUrl(id));
    }

    public DecodeResponse decode(DecodeRequest request) {
        long id = parseId(extractCode(request.url().trim()));
        return new DecodeResponse(toShortUrl(id), lookup(id));
    }

    public String resolveOriginalUrl(String code) {
        return lookup(parseId(code));
    }

    private String lookup(long id) {
        String originalUrl = idToUrl.get(id);
        if (originalUrl == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found");
        }
        return originalUrl;
    }

    private long parseId(String code) {
        try {
            return Base62.decode(code);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid short URL");
        }
    }

    private String toShortUrl(long id) {
        return baseDomain + "/" + Base62.encode(id);
    }

    private void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URL");
            }
        } catch (URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URL");
        }
    }

    /**
     * Accepts either a full short URL (https://my.pass/21) or a bare code (21)
     * and returns the trailing code segment.
     */
    private String extractCode(String input) {
        String code = input;
        int lastSlash = code.lastIndexOf('/');
        if (lastSlash >= 0) {
            code = code.substring(lastSlash + 1);
        }
        if (code.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid short URL");
        }
        return code;
    }
}
