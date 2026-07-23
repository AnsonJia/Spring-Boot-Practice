package com.codewithmosh.store.config;

import com.codewithmosh.store.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //marks 3rd party class and tells spring it contains @Bean methods so spring can create instances at runtime
@EnableWebSecurity //enables Spring Security's web security configuration.
@AllArgsConstructor
public class SecurityConfig {//at runtime when spring creates an instance of this class, it will inject UserService into UserDetailsService
    private final UserDetailsService userDetailsService;//stores our custom UserService implementation into this field
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //currently we are storing user passwords as plaintext in users table which is bad so we hash them
    @Bean//anytime we need a password encoder spring will give us BCrypt
    public PasswordEncoder passwordEncoder(){ //PasswordEncoder is an interface with methods
        return new BCryptPasswordEncoder();//BCrypt is an implementation of PasswordEncoder that is the most secure and recommended hashing algorithm
    }

    @Bean//using DaoAuthentication Provider which is the most common requires a userDetailsService(UserService) and passwordEncoder
    public AuthenticationProvider authenticationProvider(){
        //Dao finds the user and validates the password
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());//pass our password encoder defined above
        provider.setUserDetailsService(userDetailsService);//uds is an abstraction for finding a user just like a repository
        return provider;
    }

    @Bean //AuthenticationManager is the entry point for authentication and delegates login requests to the appropriate AuthenticationProvider (can provide multiple providers if needed)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {//auto looks for AuthenticationProvider beans
        return config.getAuthenticationManager();
    }

    @Bean // Registers the returned SecurityFilterChain as a Spring bean (call method during startup)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {//try catch or throw exception to be handled somewhere else
        http    .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless sessions (token-based authentication)
                .csrf(AbstractHttpConfigurer::disable) //CSRF (Cross-Site Request Forgery) common in traditional but not rest so disable to improve performance
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/carts/**").permitAll()//all endpoints matching the url will be public (** for all children)
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()//only register user endpoint (post) is public
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()//only login endpoint (post) is public
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()//allow refresh endpoint to be public (otherwise requires access token which may be expired)
                        .anyRequest().authenticated()//all other requests needs to be authenticated
                        //.anyRequest().permitAll() //all endpoints will be public
                )//authorize http requests
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)//add the filter to be the first in the chain
                .exceptionHandling(c -> //takes a lambda function
                        c.authenticationEntryPoint(//if user is not authenticated return 401 unauthorized (default is 403)
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return http.build(); //build the securityFilterChain object and return it
    }
}
