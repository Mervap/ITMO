package hello.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import hello.exception.NoSuchResourceException;
import hello.exception.ValidationException;

@ControllerAdvice
public class RestControllerExceptionHandler {
    @SuppressWarnings("unused")
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleException(NoSuchResourceException ignored) {
        return new Error("Resource not found");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            String message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
            return new Error(message);
        } else {
            return new Error("Method argument not valid");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleException(ValidationException e) {
        return new Error(e.getMessage());
    }

    public static class Error {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        @SuppressWarnings("unused")
        public String getMessage() {
            return message;
        }
    }
}
