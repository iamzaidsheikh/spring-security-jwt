package com.example.springsecurityjwt.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data //Annotation from lombock that provides getters and setters
@NoArgsConstructor //Lombok annotation
@AllArgsConstructor //Lombok annotation
public class User {
    
    //NOTE : Spring security also has a User class os just keep that in mind to import the right user class
    //One work around is to rename our User to AppUser

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    //We define this relationship of User model with Role
    //We define fetch because when we fetch the users we also need to fetch their roles
    private List<Role> roles = new ArrayList<>();
}
