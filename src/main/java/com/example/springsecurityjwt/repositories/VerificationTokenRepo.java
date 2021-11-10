package com.example.springsecurityjwt.repositories;

import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.models.VerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}