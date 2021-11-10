package com.example.springsecurityjwt.services;

import java.util.ArrayList;
import java.util.List;

import com.example.springsecurityjwt.exception.EmailAlreadyExistsException;
import com.example.springsecurityjwt.exception.UsernameAlreadyExistsException;
import com.example.springsecurityjwt.models.Role;
import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.models.VerificationToken;
import com.example.springsecurityjwt.repositories.RoleRepo;
import com.example.springsecurityjwt.repositories.UserRepo;
import com.example.springsecurityjwt.repositories.VerificationTokenRepo;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepo tokenRepo;

    private boolean emailExists(String email) {
        return userRepo.findByEmail(email) != null;
    }

    private boolean usernameExists(String username) {
        return userRepo.findByUsername(username) != null;
    }

    public User saveRegisteredUser(User user) {
        return userRepo.save(user);
    }

    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepo.findByToken(VerificationToken);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepo.save(myToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null) {
            log.error("User: {} not found", username);
            throw new UsernameNotFoundException("User: " + username + " not found");
        } else {
            log.info("User {} found", username);
        }

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        //We need to grant authorities to these roles
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName())); 
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), accountNonExpired,
        credentialsNonExpired, accountNonLocked, authorities); //Here we need to return a spring security user
    }

    @Override
    public User registerUser(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        
        //Check if email already exists
        if(emailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        } 
        
        //Check if username already exists
        else if(usernameExists(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        //Then register user
        log.info("Registering new user {} {}",user.getFirstName(), user.getLastName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
