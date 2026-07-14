package com.codewithmosh.store.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
                .issuedAt(new Date())//set timestamp when token was created
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))//multiply by 1000 because its in milliseconds
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))//provide a secret from YAML so it's not hardcoded
                .compact();//generate the token (like .build() in builder methods)
    }

    public boolean validateToken(String token) {
        try {//claims are properties we know about the token (doesn't include secret)
            var claims = getClaims(token);//extract the token's payload (claims)
            return claims.getExpiration().after(new Date());//check if token is expired before returning
        }catch (JwtException e) {//if any exceptions, token is invalid so return false
            return false;
        }
    }

    private Claims getClaims(String token) {
            //create a jwt parser
            return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))//provide a secret from YAML so it's not hardcoded
                .build()
                    //verify and parse the jwt
                .parseSignedClaims(token)
                .getPayload();//get the claims from the token
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();//we uniquely identify users by their email in the token subject
    }


}
