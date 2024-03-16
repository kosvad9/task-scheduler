package com.kosvad9.taskscheduler.configuration.securiity;

import com.kosvad9.taskscheduler.dto.Token;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class CookieFactory {
    private final JWTSerializer jwtSerializer;

    private String cookieName = "__Host-auth-token";

    public Cookie createClearCookie(String value){
        Cookie cookie = new Cookie(cookieName, value);
        setCookieSecureParameters(cookie);
        cookie.setMaxAge(0);
        return cookie;
    }

    public Cookie createCookie(Token token, Integer maxAge){
        String jwt = jwtSerializer.apply(token);
        Cookie cookie = new Cookie(cookieName, jwt);
        setCookieSecureParameters(cookie);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    private void setCookieSecureParameters(Cookie cookie){
        cookie.setSecure(true);
        cookie.setDomain(null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
