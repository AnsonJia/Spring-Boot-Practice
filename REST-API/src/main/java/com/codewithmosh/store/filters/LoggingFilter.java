package com.codewithmosh.store.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component//mark as bean
//filters are a class that runs before controllers and can inspect or modify HTTP requests
public class LoggingFilter extends OncePerRequestFilter {//extends OncePerRequestFilter to ensure that this class gets called once per request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Incoming request: " + request.getMethod() + " " + request.getRequestURI());  // Log the request details
        filterChain.doFilter(request, response); // Continue with the filter chain and eventually hit the controller
        System.out.println("Outgoing response: " + response.getStatus()); // Log the response details from the controller
    }
}
