package com.akin.akinbackend.service;

import com.akin.akinbackend.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${jwt.private.key}")
    private RSAPrivateKey key;

    public String generateToken(User user, Integer duration, String tokenType) {
        return generateToken(user.getUsername(), duration, tokenType);
    }


    public String generateToken(String username , Integer duration, String tokenType){
        try {
            Algorithm algorithm = Algorithm.HMAC256(key.getEncoded());
            String token = JWT.create()
                    .withIssuer("akin-backend")
                    .withClaim("username", username)
                    .withClaim("tokenType", tokenType)
                    .withExpiresAt(generateExpirationDate(duration))
                    .sign(algorithm);

            return token;

        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token", exception);
        }
    }


    public DecodedJWT validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(key.getEncoded());
            JWTVerifier verifier =  JWT.require(algorithm)
                    .withIssuer("akin-backend")
                    .build();

            verifier.verify(token);
            return JWT.decode(token);
        } catch (TokenExpiredException exception){
            throw new RuntimeException("Error validating token: " + exception.getMessage(), exception);
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Error while validating token", exception.getCause());
        }
    }

    public String getUsername(String token){
        return validateToken(token).getClaim("username").asString();
    }

    public String getTokenType(String token){
        return validateToken(token).getClaim("tokenType").asString();
    }

    private Instant generateExpirationDate(Integer seconds){
        return LocalDateTime.now().plusSeconds(seconds).toInstant(ZoneOffset.of("-03:00"));
    }
}
