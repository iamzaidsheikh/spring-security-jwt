package com.example.springsecurityjwt.api;

import java.net.URI;
import java.util.List;

import com.example.springsecurityjwt.models.Role;
import com.example.springsecurityjwt.models.RoleToUser;
import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api") //Base url for all the endpoints of this class
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok()
        .body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {

        //We need to send a response code 201 for some resource that's added to the server
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()  //This returns localhost:8080
        .path("api/user/save")
        .toUriString());

        return ResponseEntity.created(uri)
        .body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()  //This returns localhost:8080
        .path("api/role/save")
        .toUriString());

        return ResponseEntity.created(uri)
        .body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUser roleToUser) { //We added ? because the service returns a void

        userService.addRoleToUser(roleToUser.getUsername(), roleToUser.getRoleName()); 
        //Since body can't be void so we just return status 200
        return ResponseEntity.ok()
        .build();
    }
}
