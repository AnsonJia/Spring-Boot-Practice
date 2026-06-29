package com.codewithmosh.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //marks 3rd party class and tells spring it contains @Bean methods so spring can create instances at runtime
@EnableWebSecurity //enables Spring Security's web security configuration.
public class SecurityConfig {
    @Bean // Registers the returned SecurityFilterChain as a Spring bean (call method during startup)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {//try catch or throw exception to be handled somewhere else
        http    .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless sessions (token-based authentication)
                .csrf(AbstractHttpConfigurer::disable) //CSRF (Cross-Site Request Forgery) common in traditional but not rest so disable to improve performance
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/carts/**").permitAll()//all endpoints matching the url will be public (** for all children)
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()//only register user endpoint (post) is public
                        .anyRequest().authenticated()//all other requests needs to be authenticated
                        //.anyRequest().permitAll() //all endpoints will be public
                ); //authorize http requests
        return http.build(); //build the securityFilterChain object and return it
    }
}
