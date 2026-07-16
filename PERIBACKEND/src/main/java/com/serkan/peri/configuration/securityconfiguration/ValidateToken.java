package com.serkan.peri.configuration.securityconfiguration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ValidateToken {

    @Value("${app.jwt.secret}")
    private String tokenEncryptionKey;


    public Optional<UUID> validateToken(String jwtAuthenticationToken) {

        try {

            Algorithm decryptionAlgorithm = Algorithm.HMAC512(tokenEncryptionKey);
            JWTVerifier jwtAuthenticationTokenVerifier = JWT.require(decryptionAlgorithm).build();
            DecodedJWT decryptedJwtAuthenticationToken = jwtAuthenticationTokenVerifier.verify(jwtAuthenticationToken);

                if (Objects.isNull(decryptedJwtAuthenticationToken))
                    return Optional.empty();
                String userId = decryptedJwtAuthenticationToken.getClaim("userId").asString();
                UUID userUUID = UUID.fromString(userId);
                return Optional.of(userUUID);



        } catch (Exception exception) {
            return Optional.empty();
        }

    }
}