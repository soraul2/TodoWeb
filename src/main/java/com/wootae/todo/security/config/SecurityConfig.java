package com.wootae.todo.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http){
        //1. csrf , httpbasic , formLogin , session disble

        http.formLogin((auth)->auth.disable());
        http.httpBasic((auth)->auth.disable());
        http.csrf((auth)->auth.disable());
        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((auth)->auth.requestMatchers("/user/**","/login","/logout").permitAll());
        http.authorizeHttpRequests((auth)->auth.anyRequest().authenticated());

        http.logout((auth)->auth
                .logoutSuccessUrl("/")
                .logoutUrl("/logout")
                .deleteCookies("Authentication")
                .logoutSuccessUrl("/login")
        );

        return http.build();
    }


}
