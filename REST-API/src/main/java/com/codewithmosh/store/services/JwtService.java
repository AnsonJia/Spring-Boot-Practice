package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.User;
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

    public String generateToken(User user) { //method to generate JSON web tokens
        final long tokenExpiration = 86400; //number of seconds in 1 day
        return Jwts.builder() //builder object to build the token
                //.subject(email)//set subject to anything that can uniquely identify a user (id, email, etc.)
                .subject(user.getId().toString())//set subject to user id instead for easier lookup since it's a primary key
                .claim("email", user.getEmail())//adding extra claims on common used properties like email and name
                .claim("name", user.getName())
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
                //verify and parse the jwt
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))//provide a secret from YAML so it's not hardcoded
                .build()
                .parseSignedClaims(token)
                .getPayload();//get the payload (claims) from the token
    }

    public Long getUserIdFromToken(String token) {//gets user id from token so we can use in userpassauthtoken in jwtauthfilter
        return Long.valueOf(getClaims(token).getSubject());//we uniquely identify users by their id in the token subject
    }


}
