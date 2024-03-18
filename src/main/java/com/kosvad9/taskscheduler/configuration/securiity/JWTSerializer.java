package com.kosvad9.taskscheduler.configuration.securiity;

import com.kosvad9.taskscheduler.dto.Token;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTSerializer implements Function<Token, String> {

    private final JWEEncrypter jweEncrypter;

    private final JWEAlgorithm jweAlgorithm;

    private final EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    @Override
    public String apply(Token token) {
        JWEHeader header = new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
                .keyID(token.id().toString()).build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subjectId().toString())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .build();
        EncryptedJWT jwt = new EncryptedJWT(header,claimsSet);
        try {
            jwt.encrypt(jweEncrypter);
            return jwt.serialize();
        } catch (JOSEException e) {
            log.error(e.toString());
        }
        return null;
    }
}
