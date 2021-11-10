package com.example.springsecurityjwt.exception;

public class VerificationTokenDoesNotExistException extends RuntimeException{
    
    public VerificationTokenDoesNotExistException(String token) {
        super("Verification token: " + token + " does not exist");
    }
}
