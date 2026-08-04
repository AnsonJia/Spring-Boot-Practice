package com.codewithmosh.store.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")//prefix for properties in application.properties file
@Data
public class JwtConfig {//class for encapsulating token settings
    //properties from application.properties file
    private String secret;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    public SecretKey getSecretKey() {//method to get the secret key for signing the token
        return Keys.hmacShaKeyFor(secret.getBytes());
    }//creates a secret key using the secret string from application.properties file and converts it to a byte array
}
