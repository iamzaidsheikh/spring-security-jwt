package com.example.springsecurityjwt.exception;

public class VerificationTokenExpiredException extends RuntimeException{
    
    public VerificationTokenExpiredException(String token) {
        super("Verifcation token: " + token + " has expired");
    }
}
