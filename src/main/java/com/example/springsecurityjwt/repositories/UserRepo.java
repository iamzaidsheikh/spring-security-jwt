package com.example.springsecurityjwt.repositories;

import com.example.springsecurityjwt.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    //Spring Data JPA understands the function by how we define its name
    User findByUsername(String username);
    User findByEmail(String email);

}
