package com.kosvad9.taskscheduler.controller;

import com.kosvad9.taskscheduler.dto.ErrorDto;
import com.kosvad9.taskscheduler.exception.EmailTakenException;
import com.kosvad9.taskscheduler.exception.IncorrectUsernamePasswordException;
import com.kosvad9.taskscheduler.exception.ValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice

@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler({ValidException.class})
    public ResponseEntity<ErrorDto> validationException(ValidException e){

        String error = e.bindingResult.getFieldErrors().stream()
                .map(objectError -> objectError.getField() + " " +
                                            objectError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.error(error);
        return new ResponseEntity<>(new ErrorDto("Ошибка валидации входных данных; "+error),HttpStatus.BAD_REQUEST);
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
