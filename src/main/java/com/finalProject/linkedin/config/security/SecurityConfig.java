package com.finalProject.linkedin.config.security;

import com.finalProject.linkedin.entity.User;
import com.finalProject.linkedin.repository.UserRepository;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${FRONT_URL}")
    private String FRONT_URL;

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/login",
                                "/auth",
                                "/",
                                "/confirm",
                                "/oauth2/**",
                                "/password-forgot",
                                "/password-reset",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers("/profiles/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(this::oauth2SuccessHandler)
                        .permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profiles", true)
                        .failureHandler((request, response, exception) -> {
                            log.error("Authentication failed: {}", exception.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
                        })
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // одна неделя
                        .useSecureCookie(true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler())
                        .deleteCookies( "JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                        .permitAll());

        return http.build();
    }

    private void oauth2SuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        String email = oauthToken.getPrincipal().getAttribute("email");

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            response.sendRedirect(FRONT_URL + "/registrations");
        } else {
            response.sendRedirect(FRONT_URL + "/home");
        }
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return (HttpServletRequest req, HttpServletResponse res, Authentication authentication) -> {
            res.setStatus(OK.value());
            res.getWriter().flush();
        };
    }

    @Bean
    public UserDetailsService userDetailsService(UserServiceImpl userServiceImpl) {
        return email -> {
            Boolean isVerified = userServiceImpl.isUserVerified(email);
            if (isVerified == null || !isVerified) {
                log.warn("User not verified: {}", email);
                throw new BadCredentialsException("Email not verified!");
            }

            User user = userServiceImpl.findUserByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            log.warn("Имейл: {}", email);
            log.warn("Зашифрованный пароль из базы данных: {}", user.getPassword());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
