package com.kosvad9.taskscheduler.configuration.securiity;

import com.kosvad9.taskscheduler.dto.Token;
import com.kosvad9.taskscheduler.dto.UserDto;
import com.kosvad9.taskscheduler.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Component
public class TokenFactory implements Function<Authentication, Token> {

    private Duration tokenTTL = Duration.ofDays(10);

    @Override
    public Token apply(Authentication authentication) {
        Instant now = Instant.now();
        return new Token(UUID.randomUUID(),
                Long.parseLong(authentication.getName()),
                now,
                now.plus(tokenTTL));
    }

    public Token apply(UserDto userDto) {
        Instant now = Instant.now();
        return new Token(UUID.randomUUID(),
                userDto.id(),
                now,
                now.plus(tokenTTL));
    }

    public void setTokenTTL(Duration tokenTTL) {
        this.tokenTTL = tokenTTL;
    }
}
