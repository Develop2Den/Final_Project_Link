package com.finalProject.linkedin.config.security;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfiguration {

    @Bean
    public CookieSameSiteSupplier rememberMeCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone().whenHasName("remember-me");
    }

    @Bean
    public CookieSameSiteSupplier sessionCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone().whenHasName("JSESSIONID");
    }
}

