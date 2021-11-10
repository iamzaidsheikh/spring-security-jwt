package com.example.springsecurityjwt.registration.listeners;

import java.util.UUID;

import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.registration.OnRegistrationCompleteEvent;
import com.example.springsecurityjwt.services.UserServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserServiceImpl userService;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    /******************************************************************************
     * the confirmRegistration method will receive the OnRegistrationCompleteEvent,
     * extract all the necessary User information from it, create the verification
     * token, persist it, and then send it as a parameter in the “Confirm
     * Registration” link. MessageSource allows for internationalization of text
     * Message text will be show to the user in their preferred language Any
     * javax.mail.AuthenticationFailedException thrown by JavaMailSender will be
     * handled by the controller.
     ********************************************************************************/

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString(); // Generating token
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Confirm Registration";
        String confirmationUrl = event.getAppUrl() + "/api/confirmRegistration?token=" + token;
        String message = "Almost there. Click on the following link to activate your account";

        // Creating the email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
        log.info("Verfication link sent to email: {}", recipientAddress);
    }
}