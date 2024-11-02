package com.finalProject.linkedin.config.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        if (response instanceof HttpServletResponse httpServletResponse) {
            Collection<String> headers = httpServletResponse.getHeaders(HttpHeaders.SET_COOKIE)
                    .stream()
                    .map(header -> header + "; SameSite=None; Secure")
                    .collect(Collectors.toList());
            httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, String.join(",", headers));
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}

