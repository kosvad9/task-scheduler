package com.kosvad9.taskscheduler.configuration.securiity;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.text.ParseException;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity,
                                         JWTCookieAuthConfigurer jwtCookieAuthConfigurer) throws Exception {

        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/user").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET,"/user").authenticated()
                        .requestMatchers("/task/**").authenticated()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy((authentication, request, response) -> {}))
                .addFilterAfter(new GetCSRFTokenFilter(), ExceptionTranslationFilter.class)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        httpSecurity.apply(jwtCookieAuthConfigurer);

        return httpSecurity.build();
    }

    @Bean
    public JWEEncrypter jweEncrypter(@Value("${jwt.secretKey}") String key) throws ParseException, KeyLengthException {
        return new DirectEncrypter(OctetSequenceKey.parse(key));
    }

    @Bean
    public JWEAlgorithm jweAlgorithm(){
        return JWEAlgorithm.DIR;
    }

    @Bean
    public JWEDecrypter jweDecrypter(@Value("${jwt.secretKey}") String key) throws ParseException, KeyLengthException {
        return new DirectDecrypter(OctetSequenceKey.parse(key));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
