package com.example.springsecurityjwt.services;

import java.util.List;

import com.example.springsecurityjwt.models.Role;
import com.example.springsecurityjwt.models.User;

public interface UserService {
    
    //Here we declare all the methods that we need to manage the application
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();

    //NOTE : In large apps we dont retrieve all users at once as there can be millions of users
    //Instead we do it batch wise. Retrieve a few users at a time and repeat the process as required

}
