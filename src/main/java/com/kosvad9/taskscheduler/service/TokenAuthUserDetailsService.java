package com.kosvad9.taskscheduler.service;

import com.kosvad9.taskscheduler.dto.Token;
import com.kosvad9.taskscheduler.dto.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class TokenAuthUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token){
            var userToken = new UserToken(token.subjectId().toString(), "", true, true,
                    !jdbcTemplate.queryForObject("""
                    select exists(select * from tasks.deactivated_tokens where id = ?)
                    """, Boolean.class, token.id())
                            && token.expiresAt().isAfter(Instant.now()),
                    true,
                    Collections.emptyList(), token);
            return userToken;
        }
        throw new UsernameNotFoundException("Principal must be Token");
    }
}
