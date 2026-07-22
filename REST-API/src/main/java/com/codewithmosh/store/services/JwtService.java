package com.codewithmosh.store.services;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    //@Value("${spring.jwt.secret}")//inject the secret from application.yaml
    //private String secret;
    private final JwtConfig jwtConfig;//needs allargs

    public String generateAccessToken(User user) { //method to generate JSON web tokens
        //final long tokenExpiration = 300; //number of seconds in 5 minutes (short-lived token to be more secure since its exposed)
        return generateToken(user, jwtConfig.getAccessTokenExpiration());//get expiration from config class instead of hardcode
    }

    public String generateRefreshToken(User user) { //method to generate refresh tokens
        //final long tokenExpiration = 604800; //number of seconds in 7 days (long-lived since it's not exposed)
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder() //builder object to build the token
                //.subject(email)//set subject to anything that can uniquely identify a user (id, email, etc.)
                .subject(user.getId().toString())//set subject to user id instead for easier lookup since it's a primary key
                .claim("email", user.getEmail())//adding extra claims on common used properties like email and name
                .claim("name", user.getName())
                .issuedAt(new Date())//set timestamp when token was created
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))//multiply by 1000 because its in milliseconds
                .signWith(jwtConfig.getSecretKey())//provide a secret from YAML so it's not hardcoded
                .compact();
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
                .verifyWith(jwtConfig.getSecretKey())//provide a secret from YAML so it's not hardcoded
                .build()
                .parseSignedClaims(token)
                .getPayload();//get the payload (claims) from the token
    }

    public Long getUserIdFromToken(String token) {//gets user id from token so we can use in userpassauthtoken in jwtauthfilter
        return Long.valueOf(getClaims(token).getSubject());//we uniquely identify users by their id in the token subject
    }


}
