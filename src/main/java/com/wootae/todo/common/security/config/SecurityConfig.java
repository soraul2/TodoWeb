package com.wootae.todo.common.security.config;

import com.wootae.todo.common.security.filter.JwtFilter;
import com.wootae.todo.common.security.filter.LoginFilter;
import com.wootae.todo.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

        //1. csrf , httpbasic , formLogin , session disble

        http.formLogin((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());
        http.csrf((auth) -> auth.disable());
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterAt(new LoginFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/user/**", "/login", "/logout","/main/**").permitAll());
        http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated());

        http.logout((auth) -> auth
                .logoutSuccessUrl("/")
                .logoutUrl("/logout")
                .deleteCookies("Authentication")
                .logoutSuccessUrl("/user/login")
        );

        return http.build();
    }


}
