package com.example.springsecurityjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<Error> exception(UsernameAlreadyExistsException e) {
        Error error = new Error(HttpStatus.CONFLICT, "Username already exists", e);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    public ResponseEntity<Error> exception(EmailAlreadyExistsException e) {
        Error error = new Error(HttpStatus.CONFLICT, "Email already exists", e);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = VerificationTokenDoesNotExistException.class)
    public ResponseEntity<Error> exception(VerificationTokenDoesNotExistException e) {
        Error error = new Error(HttpStatus.NOT_FOUND, "Verification token does not exist", e);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(value = VerificationTokenExpiredException.class)
    public ResponseEntity<Error> exception(VerificationTokenExpiredException e) {
        Error error = new Error(HttpStatus.FORBIDDEN, "Verification token has expired", e);
        return new ResponseEntity<>(error, error.getStatus());
    }
}
