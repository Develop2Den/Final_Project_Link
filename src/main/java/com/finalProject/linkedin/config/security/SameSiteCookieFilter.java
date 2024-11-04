package com.finalProject.linkedin.config.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Оборачиваем ответ, чтобы модифицировать заголовок Set-Cookie
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(httpServletResponse) {
            @Override
            public void addHeader(String name, String value) {
                if ("Set-Cookie".equalsIgnoreCase(name)) {
                    value = value + "; SameSite=None; Secure";
                }
                super.addHeader(name, value);
            }
        };

        chain.doFilter(request, responseWrapper);
    }
}



