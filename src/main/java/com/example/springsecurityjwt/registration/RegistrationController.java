package com.example.springsecurityjwt.registration;

import java.util.Calendar;

import com.example.springsecurityjwt.exception.VerificationTokenDoesNotExistException;
import com.example.springsecurityjwt.exception.VerificationTokenExpiredException;
import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.models.VerificationToken;
import com.example.springsecurityjwt.services.UserServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {
    private final UserServiceImpl userService;

    @GetMapping("/confirmRegistration")
    public ResponseEntity<User> confirmRegistration (@RequestParam("token") String token) throws VerificationTokenDoesNotExistException, VerificationTokenExpiredException{
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            //Return a verification token does not exist error
            throw new VerificationTokenDoesNotExistException(token);
        }
        
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            //return a verification token expired error
            throw new VerificationTokenExpiredException(token);
        } 
        
        //On success return enabled user
        user.setEnabled(true); 
        userService.saveRegisteredUser(user); 
        log.info("{} verified. Account enabled", user.getEmail());
        return ResponseEntity.ok(user);
    }
}
