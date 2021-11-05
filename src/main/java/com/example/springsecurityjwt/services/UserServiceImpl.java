package com.example.springsecurityjwt.services;

import java.util.List;

import com.example.springsecurityjwt.models.Role;
import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.repositories.RoleRepo;
import com.example.springsecurityjwt.repositories.UserRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service //Stereotype annotation
@RequiredArgsConstructor //lombok
@Transactional //spring annotation 
@Slf4j //logger
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {}",user.getName());
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {}", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        log.info("Adding role {} to the user {}", roleName, username);
        user.getRoles().add(role); //because of @Transactional we don't need to call save() it calls it automatically
        
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }
    
}
