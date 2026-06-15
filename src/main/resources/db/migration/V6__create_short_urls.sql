CREATE TABLE short_urls (
    id           BIGSERIAL PRIMARY KEY,
    original_url TEXT      NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_short_urls_original_url ON short_urls (original_url);
