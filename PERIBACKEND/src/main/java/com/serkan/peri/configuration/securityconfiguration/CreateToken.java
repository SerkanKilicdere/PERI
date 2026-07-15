package com.serkan.peri.configuration.securityconfiguration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CreateToken {
    private final String tokenEncryptionKey = "}YtuvEt_)X~II'=NGrP&PKh1!,Kc$ksSCjCF0h.3";
    private final String owner = "PERI";
    private final Long duration = 1000L * 60 * 10;

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