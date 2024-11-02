package com.finalProject.linkedin.config.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Log4j2
@Component
public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        if (response instanceof HttpServletResponse httpServletResponse) {

            // Получаем заголовки Set-Cookie
            String[] cookies = httpServletResponse.getHeaders("Set-Cookie").toArray(new String[0]);
            log.warn("Cookies: " + Arrays.toString(cookies));
            for (String cookie : cookies) {
                // Добавляем атрибут SameSite=None
                httpServletResponse.setHeader("Set-Cookie", cookie + "; SameSite=None");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}


