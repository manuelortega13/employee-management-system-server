package com.ethan.employee_system.urlshortener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlShortenerServiceTest {

    private static final String LONG_URL = "https://docs.mypassglobal.com/api/index.html";

    private UrlShortenerService service;

    @BeforeEach
    void setUp() {
        service = new UrlShortenerService();
        ReflectionTestUtils.setField(service, "baseDomain", "https://my.pass");
    }

    @Test
    void encodeReturnsShortUrlForNewUrl() {
        EncodeResponse response = service.encode(new EncodeRequest(LONG_URL));

        assertThat(response.originalUrl()).isEqualTo(LONG_URL);
        assertThat(response.shortUrl()).startsWith("https://my.pass/");
    }

    @Test
    void encodeIsIdempotentForSameUrl() {
        String first = service.encode(new EncodeRequest(LONG_URL)).shortUrl();
        String second = service.encode(new EncodeRequest(LONG_URL)).shortUrl();

        assertThat(second).isEqualTo(first);
    }

    @Test
    void encodeAssignsDistinctCodesToDistinctUrls() {
        String a = service.encode(new EncodeRequest("https://example.com/a")).shortUrl();
        String b = service.encode(new EncodeRequest("https://example.com/b")).shortUrl();

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void encodeTrimsWhitespace() {
        String trimmed = service.encode(new EncodeRequest(LONG_URL)).shortUrl();
        String padded = service.encode(new EncodeRequest("  " + LONG_URL + "  ")).shortUrl();

        assertThat(padded).isEqualTo(trimmed);
    }

    @Test
    void encodeRejectsMalformedUrl() {
        assertThatThrownBy(() -> service.encode(new EncodeRequest("not a url")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400");
    }

    @Test
    void decodeReturnsOriginalUrlForFullShortUrl() {
        String shortUrl = service.encode(new EncodeRequest(LONG_URL)).shortUrl();

        DecodeResponse response = service.decode(new DecodeRequest(shortUrl));

        assertThat(response.shortUrl()).isEqualTo(shortUrl);
        assertThat(response.originalUrl()).isEqualTo(LONG_URL);
    }

    @Test
    void decodeAcceptsBareCode() {
        String shortUrl = service.encode(new EncodeRequest(LONG_URL)).shortUrl();
        String code = shortUrl.substring(shortUrl.lastIndexOf('/') + 1);

        DecodeResponse response = service.decode(new DecodeRequest(code));

        assertThat(response.originalUrl()).isEqualTo(LONG_URL);
    }

    @Test
    void decodeRejectsInvalidCode() {
        assertThatThrownBy(() -> service.decode(new DecodeRequest("https://my.pass/!!!")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400");
    }

    @Test
    void decodeReturnsNotFoundForUnknownCode() {
        assertThatThrownBy(() -> service.decode(new DecodeRequest("https://my.pass/zzzzz")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    void resolveOriginalUrlReturnsStoredUrl() {
        String shortUrl = service.encode(new EncodeRequest(LONG_URL)).shortUrl();
        String code = shortUrl.substring(shortUrl.lastIndexOf('/') + 1);

        assertThat(service.resolveOriginalUrl(code)).isEqualTo(LONG_URL);
    }
}
