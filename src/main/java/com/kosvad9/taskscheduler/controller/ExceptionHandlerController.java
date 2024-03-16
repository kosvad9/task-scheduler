package com.kosvad9.taskscheduler.controller;

import com.kosvad9.taskscheduler.dto.ErrorDto;
import com.kosvad9.taskscheduler.exception.EmailTakenException;
import com.kosvad9.taskscheduler.exception.IncorrectUsernamePasswordException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorDto> validationException(ValidationException e){
        log.error(e.toString());
        return new ResponseEntity<>(new ErrorDto("Ошибка валидации входных данных"),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmailTakenException.class})
    public ResponseEntity<ErrorDto> emailAlreadyTakenException(EmailTakenException e){
        log.error(e.toString());
        return new ResponseEntity<>(new ErrorDto("Пользователь уже зарегистрирован"),HttpStatus.CONFLICT);
    }

    @ExceptionHandler({IncorrectUsernamePasswordException.class})
    public ResponseEntity<ErrorDto> incorrectUsernamePasswordException(IncorrectUsernamePasswordException e){
        log.error(e.toString());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()),HttpStatus.UNAUTHORIZED);
    }
}
