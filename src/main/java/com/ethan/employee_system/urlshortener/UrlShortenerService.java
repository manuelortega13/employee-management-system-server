package com.ethan.employee_system.urlshortener;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository repository;

    @Value("${urlshortener.base-domain:https://my.pass}")
    private String baseDomain;

    @Transactional
    public EncodeResponse encode(EncodeRequest request) {
        String originalUrl = request.url().trim();
        validateUrl(originalUrl);

        ShortUrl shortUrl = repository.findFirstByOriginalUrl(originalUrl)
                .orElseGet(() -> {
                    ShortUrl entity = new ShortUrl();
                    entity.setOriginalUrl(originalUrl);
                    return repository.save(entity);
                });

        return new EncodeResponse(originalUrl, toShortUrl(shortUrl.getId()));
    }

    @Transactional(readOnly = true)
    public DecodeResponse decode(DecodeRequest request) {
        ShortUrl shortUrl = resolve(extractCode(request.url().trim()));
        return new DecodeResponse(toShortUrl(shortUrl.getId()), shortUrl.getOriginalUrl());
    }

    @Transactional(readOnly = true)
    public String resolveOriginalUrl(String code) {
        return resolve(code).getOriginalUrl();
    }

    private ShortUrl resolve(String code) {
        long id;
        try {
            id = Base62.decode(code);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid short URL");
        }

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));
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
