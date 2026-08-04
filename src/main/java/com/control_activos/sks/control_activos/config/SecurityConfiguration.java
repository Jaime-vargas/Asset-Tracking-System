package com.control_activos.sks.control_activos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HTTP FILTER CHAIN CONFIGURATION
        http
            .csrf(AbstractHttpConfigurer::disable)
                // JWT = STATELESS
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                            ))
                // ACCESS RULES
            .authorizeHttpRequests(auth -> auth
                    //temp for view images

                    .requestMatchers("/uploads/**").permitAll()
                    .requestMatchers("/api/v1/login").permitAll()
                    .requestMatchers("/api/v1/**").authenticated()
                    .requestMatchers("/**").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
            ).addFilterBefore(
                authenticationFilter, UsernamePasswordAuthenticationFilter.class
                );
        corsConfiguration(http);
        return http.build();
    }

    public void corsConfiguration(HttpSecurity http) throws Exception {
        http.cors(cors -> cors
            .configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowedOrigins(java.util.List.of("*"));
                corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfig.setAllowedHeaders(java.util.List.of("*"));
                corsConfig.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION));
                return corsConfig;
            })
        );
    }
}
