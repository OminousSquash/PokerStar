package com.varun.pokerstars.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/**").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.disable())
                .formLogin(formLoginConfigurer -> formLoginConfigurer.disable());
        return http.build();
    }
}
