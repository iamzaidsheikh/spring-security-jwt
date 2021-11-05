package com.example.springsecurityjwt.repositories;

import com.example.springsecurityjwt.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByName(String name); 
    
}
