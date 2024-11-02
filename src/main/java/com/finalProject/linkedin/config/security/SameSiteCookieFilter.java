package com.finalProject.linkedin.config.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            // Получаем текущие заголовки Set-Cookie и добавляем SameSite=None
            String setCookieHeader = httpServletResponse.getHeader("Set-Cookie");
            if (setCookieHeader != null) {
                httpServletResponse.setHeader("Set-Cookie", setCookieHeader + "; SameSite=None; Secure");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}


