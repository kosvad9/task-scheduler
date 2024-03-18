package com.kosvad9.taskscheduler.exception;

import org.springframework.validation.BindingResult;

public class ValidException extends RuntimeException{
    public final BindingResult bindingResult;

    public ValidException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public ValidException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public ValidException(String message, Throwable cause, BindingResult bindingResult) {
        super(message, cause);
        this.bindingResult = bindingResult;
    }

}
