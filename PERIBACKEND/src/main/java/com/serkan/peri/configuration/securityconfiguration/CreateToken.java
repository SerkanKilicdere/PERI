package com.serkan.peri.configuration.securityconfiguration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CreateToken {
    @Value("${app.jwt.secret}")
    private String tokenEncryptionKey;

    @Value("${app.jwt.issuer:PERI}")
    private String owner;

    @Value("${app.jwt.duration-ms:600000}")
    private Long duration;

    public String createToken(UUID userId , String emailaddress , List<String> roles  ) {
        Date tokenCreationTime = new Date();
        Date tokenExpirationTime = new Date(tokenCreationTime.getTime() + duration);
        Algorithm encrytionAlgorithm = Algorithm.HMAC512(tokenEncryptionKey);
        String jwtAuthenticationToken;
        jwtAuthenticationToken = JWT.create()
                .withIssuer(owner)
                .withIssuedAt(tokenCreationTime)
                .withExpiresAt(tokenExpirationTime)
                .withClaim("userId", userId.toString())
                .withClaim("emailaddress", emailaddress)
                .withClaim("role", roles)
                .sign(encrytionAlgorithm);
        return jwtAuthenticationToken;

    }
}