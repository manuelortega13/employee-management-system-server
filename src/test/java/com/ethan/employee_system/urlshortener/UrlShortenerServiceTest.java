package com.ethan.employee_system.urlshortener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    private static final String LONG_URL = "https://docs.mypassglobal.com/api/index.html";

    @Mock
    private ShortUrlRepository repository;

    @InjectMocks
    private UrlShortenerService service;

    @BeforeEach
    void setBaseDomain() {
        ReflectionTestUtils.setField(service, "baseDomain", "https://my.pass");
    }

    private ShortUrl entity(long id, String url) {
        ShortUrl e = new ShortUrl();
        ReflectionTestUtils.setField(e, "id", id);
        e.setOriginalUrl(url);
        return e;
    }

    @Test
    void encodeStoresNewUrlAndReturnsShortUrl() {
        when(repository.findFirstByOriginalUrl(LONG_URL)).thenReturn(Optional.empty());
        when(repository.save(any(ShortUrl.class))).thenReturn(entity(125, LONG_URL));

        EncodeResponse response = service.encode(new EncodeRequest(LONG_URL));

        assertThat(response.originalUrl()).isEqualTo(LONG_URL);
        assertThat(response.shortUrl()).isEqualTo("https://my.pass/" + Base62.encode(125));
    }

    @Test
    void encodeReusesExistingUrlWithoutSaving() {
        when(repository.findFirstByOriginalUrl(LONG_URL)).thenReturn(Optional.of(entity(7, LONG_URL)));

        EncodeResponse response = service.encode(new EncodeRequest(LONG_URL));

        assertThat(response.shortUrl()).isEqualTo("https://my.pass/" + Base62.encode(7));
        verify(repository, never()).save(any());
    }

    @Test
    void encodeTrimsWhitespace() {
        when(repository.findFirstByOriginalUrl(LONG_URL)).thenReturn(Optional.of(entity(1, LONG_URL)));

        service.encode(new EncodeRequest("  " + LONG_URL + "  "));

        verify(repository).findFirstByOriginalUrl(LONG_URL);
    }

    @Test
    void encodeRejectsMalformedUrl() {
        assertThatThrownBy(() -> service.encode(new EncodeRequest("not a url")))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400");
    }

    @Test
    void decodeReturnsOriginalUrlForFullShortUrl() {
        when(repository.findById(125L)).thenReturn(Optional.of(entity(125, LONG_URL)));

        DecodeResponse response = service.decode(new DecodeRequest("https://my.pass/" + Base62.encode(125)));

        assertThat(response.originalUrl()).isEqualTo(LONG_URL);
        assertThat(response.shortUrl()).isEqualTo("https://my.pass/" + Base62.encode(125));
    }

    @Test
    void decodeAcceptsBareCode() {
        when(repository.findById(125L)).thenReturn(Optional.of(entity(125, LONG_URL)));

        DecodeResponse response = service.decode(new DecodeRequest(Base62.encode(125)));

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
        when(repository.findById(125L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.decode(new DecodeRequest("https://my.pass/" + Base62.encode(125))))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    void resolveOriginalUrlReturnsStoredUrl() {
        when(repository.findById(125L)).thenReturn(Optional.of(entity(125, LONG_URL)));

        assertThat(service.resolveOriginalUrl(Base62.encode(125))).isEqualTo(LONG_URL);
    }
}
