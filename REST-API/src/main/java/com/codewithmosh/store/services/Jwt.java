package com.codewithmosh.store.services;//in service folder since it's closely related to jwtService class

import com.codewithmosh.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class Jwt {//new class that encapsulates token related behavior (move utility functions out of jwtService class)
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {//constructor to initialize final fields once
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired(){//checks if token is valid if it hasn't expired
        return claims.getExpiration().before(new java.util.Date());
    }

    public Long getUserId(){//gets user id from token so we can use in userpassauthtoken in jwtauthfilter
        return Long.valueOf(claims.getSubject());//we uniquely identify users by their id in the token subject
    }

    public Role getRole (){//method to get role from user token to use in authfilter for authorities
        return Role.valueOf(claims.get("role", String.class));//get role from token claims and parse into Role object
    }

    public String toString(){//token to string method
        return Jwts.builder().claims(claims).signWith(secretKey).compact(); //.claims contain all our claims vs .claim to add individually
    }

}
