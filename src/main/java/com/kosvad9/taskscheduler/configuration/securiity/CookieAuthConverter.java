package com.kosvad9.taskscheduler.configuration.securiity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CookieAuthConverter implements AuthenticationConverter {

    private final JWTDeserializer jwtDeserializer;

    private final List<RequestMatcher> matchers = List.of(new AntPathRequestMatcher("/auth/login", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/user", HttpMethod.POST.name()));
    @Override
    public Authentication convert(HttpServletRequest request) {
        boolean matchLogin = matchers.stream().anyMatch(requestMatcher -> requestMatcher.matches(request));
        if (matchLogin) return null;
        if (request.getCookies() != null && request.getCookies().length != 0){
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("__Host-auth-token"))
                    .findFirst()
                    .map(cookie -> {
                        var token = jwtDeserializer.apply(cookie.getValue());
                        return new PreAuthenticatedAuthenticationToken(token, cookie.getValue());
                    }).orElse(null);
        }
        return null;
    }
}
