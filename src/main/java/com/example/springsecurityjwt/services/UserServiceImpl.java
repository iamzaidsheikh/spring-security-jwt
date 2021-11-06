package com.example.springsecurityjwt.services;

import java.util.ArrayList;
import java.util.List;

import com.example.springsecurityjwt.models.Role;
import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.repositories.RoleRepo;
import com.example.springsecurityjwt.repositories.UserRepo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service //Stereotype annotation
@RequiredArgsConstructor //lombok
@Transactional //spring annotation 
@Slf4j //logger
public class UserServiceImpl implements UserService, UserDetailsService{

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User {} found", username);
        }

        //We need to grant authorities to these roles
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName())); 
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities); //Here we need to return a spring security user
    }

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
