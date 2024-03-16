package com.kosvad9.taskscheduler.configuration.securiity;

import com.kosvad9.taskscheduler.dto.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTDeserializer implements Function<String, Token> {

    private final JWEDecrypter jweDecrypter;

    @Override
    public Token apply(String s) {
        try {
            EncryptedJWT jwt = EncryptedJWT.parse(s);
            jwt.decrypt(jweDecrypter);
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            return new Token(UUID.fromString(claims.getJWTID()),
                    Long.parseLong(claims.getSubject()),
                    claims.getIssueTime().toInstant(),
                    claims.getExpirationTime().toInstant());

        } catch (ParseException | JOSEException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
