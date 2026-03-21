
package com.example.nutikas_restorani_serveerimissysteem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Changes spring-boot-starter-security's behaviour. 
// Only admin needs authentication, other pages don't.
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.permitAll());

        return http.build();
    }
}

// Class created with the help of https://spring.io/guides/gs/securing-web (+ google)