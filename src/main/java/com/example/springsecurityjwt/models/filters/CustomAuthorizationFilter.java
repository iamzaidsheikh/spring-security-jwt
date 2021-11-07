package com.example.springsecurityjwt.models.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // This filter intecepts every request that comes to the server

        // We dont want to check for login path because its accessable to all
        if (request.getServletPath().equals("/api/login")) {

            // We pass this request through the filter
            filterChain.doFilter(request, response);
        } else {

            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null) {
                try {
                    String token = authorizationHeader; 
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // We need to use the same signing
                                                                                  // token that issued the jwts
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token); // Verifying if the token is valid
                    String username = decodedJWT.getSubject(); // because we set the subject as our username when we
                                                               // issued the token
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class); // We set the key for claims as
                                                                                         // "roles"
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role)); // We need to convert the roles in the token
                                                                           // to Granted Authorities
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, authorities); // We are not passing the password because we get the token
                                                          // only after verifying the password
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Spring will check the
                                                                                               // roles of the user and
                                                                                               // determine what
                                                                                               // resources they can
                                                                                               // access
                    filterChain.doFilter(request, response); // Passing the request

                } catch (Exception e) {
                    log.error("Error loggin in: {}", e.getMessage());
                    
                    //We can send the response as headers
                    //response.setHeader("error", e.getMessage());
                    //response.sendError(403, "Forbidden");

                    //or in body
                    response.setStatus(403);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), error);

                    //New we add this filter to our config
                }

            } else {
                filterChain.doFilter(request, response); // If the request does not contain a token then we do nothing 
            }

        }

    }

}
