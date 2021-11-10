package com.example.springsecurityjwt.registration;

import com.example.springsecurityjwt.models.User;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent{

    private String appUrl;
    private User user;

    public OnRegistrationCompleteEvent(
      User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
    
}
