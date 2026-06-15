package com.ethan.employee_system.urlshortener;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findFirstByOriginalUrl(String originalUrl);
}
