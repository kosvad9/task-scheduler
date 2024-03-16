package com.kosvad9.taskscheduler.exception;

public class IncorrectUsernamePasswordException extends RuntimeException{
    public IncorrectUsernamePasswordException() {
    }

    public IncorrectUsernamePasswordException(String message) {
        super(message);
    }

    public IncorrectUsernamePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectUsernamePasswordException(Throwable cause) {
        super(cause);
    }

    public IncorrectUsernamePasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
