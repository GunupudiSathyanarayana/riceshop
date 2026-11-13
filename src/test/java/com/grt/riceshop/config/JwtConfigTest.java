package com.grt.riceshop.config;

import java.time.Instant;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtConfigTest {

    @Test
    void encodedTokenCanBeValidatedWithConfiguredSecret() {
        JwtConfig config = new JwtConfig();
        String encodedSecret = Base64.getEncoder().encodeToString(new byte[32]);
        SecretKey secretKey = config.jwtSecretKey(encodedSecret);
        var encoder = config.jwtEncoder(secretKey);
        var decoder = config.jwtDecoder(secretKey);
        Instant now = Instant.now();

        String token = encoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                JwtClaimsSet.builder()
                        .issuer("riceshop")
                        .subject("customer@example.com")
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(900))
                        .build()
        )).getTokenValue();

        assertEquals("customer@example.com", decoder.decode(token).getSubject());
    }
}
