package com.finalProject.linkedin.config.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse res) {
            // Получаем все Set-Cookie заголовки
            for (String header : res.getHeaders("Set-Cookie")) {
                // Добавляем SameSite=None; Secure к каждому заголовку Set-Cookie
                String updatedHeader = header + "; SameSite=None; Secure";
                res.addHeader("Set-Cookie", updatedHeader);
            }
        }
        chain.doFilter(request, response);
    }
}



