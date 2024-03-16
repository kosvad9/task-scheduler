package com.kosvad9.taskscheduler.configuration.securiity;

import com.kosvad9.taskscheduler.dto.Token;
import com.kosvad9.taskscheduler.dto.UserToken;
import com.kosvad9.taskscheduler.service.TokenAuthUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.CharSetUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JWTCookieAuthConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

    private final JdbcTemplate jdbcTemplate;

    private final CookieAuthConverter cookieAuthConverter;

    private final TokenAuthUserDetailsService tokenAuthUserDetailsService;

    private final TokenFactory jwtFactory;

    private final CookieFactory cookieFactory;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        Cookie cookie = cookieFactory.createClearCookie("");
        builder.logout(logout -> logout
                .addLogoutHandler(new CookieClearingLogoutHandler(cookie))
                .addLogoutHandler(
                        ((request, response, authentication) ->{
                            if (authentication != null &&
                                    authentication.getPrincipal() instanceof UserToken user){
                                jdbcTemplate.update("""
                                        insert into tasks.deactivated_tokens(id, expires_at) values (?, ?)
                                        on conflict (id) do update
                                        set expires_at = EXCLUDED.expires_at;
                                        """,
                                        user.getToken().id(), Date.from(user.getToken().expiresAt()));
                            }
                            response.setStatus(HttpServletResponse.SC_ACCEPTED);
                        })
                )
                .logoutUrl("/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.ACCEPTED)));
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationFilter cookieAuthFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                cookieAuthConverter);
        cookieAuthFilter.setSuccessHandler((request, response, authentication) -> {
            if (authentication.getPrincipal() instanceof UserToken userToken){
                if (ChronoUnit.HOURS.between(Instant.now(),userToken.getToken().expiresAt()) <= 3){
                    Token newToken = jwtFactory.apply(authentication);
                    Cookie cookie = cookieFactory.createCookie(newToken,
                            (int)ChronoUnit.SECONDS.between(Instant.now(),
                                                        userToken.getToken().expiresAt()));
                    response.addCookie(cookie);
                }
            }
        });
        cookieAuthFilter.setFailureHandler(
                //new AuthenticationEntryPointFailureHandler(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
                (request, response, authenticationException) -> {
                    if (authenticationException instanceof CredentialsExpiredException ||
                        authenticationException instanceof AuthenticationCredentialsNotFoundException){
                        response.setCharacterEncoding(Charset.defaultCharset().name());
                        response.getWriter().write(authenticationException.getMessage());
                    }
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                });
        var authProvider = new PreAuthenticatedAuthenticationProvider();
        authProvider.setPreAuthenticatedUserDetailsService(tokenAuthUserDetailsService);
        builder.addFilterAfter(cookieAuthFilter, CsrfFilter.class)
                .authenticationProvider(authProvider);
    }
}
