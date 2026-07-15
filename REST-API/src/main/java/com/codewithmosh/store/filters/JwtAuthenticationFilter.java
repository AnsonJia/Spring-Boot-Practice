package com.codewithmosh.store.filters;

import com.codewithmosh.store.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor//all args constructor for JwtService
public class JwtAuthenticationFilter extends OncePerRequestFilter {//call once per request
    private final JwtService jwtService;//jwtService class for validate token method

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");//extract the auth header from the request
        //if header is null or doesn't start with bearer, pass request to next filter in chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);//spring security will now check if the endpoint is protected and deny access
            return;
        }
        var token = authHeader.replace("Bearer ", "");//if auth header exists, extract token and validate
        if (!jwtService.validateToken(token)) { //if token is invalid, pass request to next filter in chain
            filterChain.doFilter(request, response);//let spring security process the request
            return;
        }
        //at this point it is a valid token
        var authentication = new UsernamePasswordAuthenticationToken( //create an authentication object
                //userpassauthtoken has 2 implementation (principle, credentials) and (principle, credentials, authorities)
                //first is used for login passing username/email/user and password, second is used for authenticated user
                jwtService.getEmailFromToken(token),
                null,
                null
        ); //when representing authenticated user, we don't need credentials and ignore authorities for now

        authentication.setDetails(//boilerplate code
                new WebAuthenticationDetailsSource().buildDetails(request)//attaching extra metadata to auth object
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);//store info about currently auth user

        filterChain.doFilter(request, response);//pass control to the next filter in the chain
    }
}
