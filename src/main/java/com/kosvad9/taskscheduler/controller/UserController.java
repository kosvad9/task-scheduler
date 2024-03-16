package com.kosvad9.taskscheduler.controller;

import com.kosvad9.taskscheduler.configuration.securiity.CookieFactory;
import com.kosvad9.taskscheduler.configuration.securiity.JWTSerializer;
import com.kosvad9.taskscheduler.configuration.securiity.TokenFactory;
import com.kosvad9.taskscheduler.dto.Token;
import com.kosvad9.taskscheduler.dto.UserLoginDto;
import com.kosvad9.taskscheduler.dto.UserDto;
import com.kosvad9.taskscheduler.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenFactory jwtFactory;
    private final CookieFactory cookieFactory;

    @GetMapping("/user")
    public UserDto getUser(@AuthenticationPrincipal UserDetails userDetails){
        return userService.getUser(Long.parseLong(userDetails.getUsername()));
    }

    @PostMapping("/user")
    public void createUser(@RequestBody @Validated UserLoginDto userLoginDto,
                           BindingResult bindingResult, HttpServletResponse httpServletResponse){
        if (bindingResult.hasErrors()) throw new ValidationException();
        UserDto user = userService.createUser(userLoginDto);
        httpServletResponse.addCookie(generateCookieToken(user));
    }

    @PostMapping("/auth/login")
    public void login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse httpServletResponse){
        UserDto user = userService.login(userLoginDto);
        httpServletResponse.addCookie(generateCookieToken(user));
    }
    @PostMapping("/user/test")
    public String test(){
        return "hello";
    }

    private Cookie generateCookieToken(UserDto userDto){
        Token token = jwtFactory.apply(userDto);
        Cookie cookie = cookieFactory.createCookie(token,
                (int) ChronoUnit.SECONDS.between(Instant.now(),token.expiresAt()));
        return cookie;
    }
}
