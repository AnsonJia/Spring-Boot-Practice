package com.codewithmosh.store.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")//inject the secret from application.yaml
    private String secret;

    public String generateToken(String email) { //method to generate JSON web tokens
        final long tokenExpiration = 86400; //number of seconds in 1 day
        return Jwts.builder() //builder object to build the token
                .subject(email)//set subject to anything that can uniquely identify a user (id, email, etc.)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))//multiply by 1000 because its in milliseconds
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))//provide a secret from YAML so it's not hardcoded
                .compact();//generate the token (like .build() in builder methods)
    }
}
