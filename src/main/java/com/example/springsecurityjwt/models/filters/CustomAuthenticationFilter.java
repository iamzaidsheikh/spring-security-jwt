package com.example.springsecurityjwt.models.filters;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import antlr.collections.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Login attempt with username: {} and password {}", username, password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws IOException, ServletException {

        // Here the User is from Spring security
        User user = (User) authentication.getPrincipal(); // Principal is the user that has been successfully
                                                          // authenticated
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // Algorithm to sign jwts
        // The secret needs to be a more secure password and it should be saved in a
        // secure location

        // Creating an access token
        String access_token = JWT.create().withSubject(user.getUsername())// Subject needs to be a string that uniquely
                                                                          // identifies a user
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // Passing the current time of the
                                                                                      // system and adding 10 mins to it
                // NOTE : this is the access token and it has to have a short expiration time
                .withIssuer(request.getRequestURL().toString()) // Author of the token
                // Now we pass all the roles of that user
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        // Creating a refresh token
        String refresh_token = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // we have passed 60 mins
                // NOTE : this is the refresh token and it has to have a long expiration time
                .withIssuer(request.getRequestURL().toString()).sign(algorithm);

        //Sending the tokens as response
        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);        
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        // Here we implement what happens at unsuccessful logins
        super.unsuccessfulAuthentication(request, response, failed);

    }

}
