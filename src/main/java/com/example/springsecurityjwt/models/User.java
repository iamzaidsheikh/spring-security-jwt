package com.example.springsecurityjwt.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.springsecurityjwt.validation.ValidEmail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity 
@Data //Annotation from lombock that provides getters and setters 
@AllArgsConstructor //Lombok annotation
public class User {
    
    //NOTE : Spring security also has a User class os just keep that in mind to import the right user class
    //One work around is to rename our User to AppUser

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    @ValidEmail //This is our custom email validator annotation
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    public User() {
        super();
        this.enabled=false; //On User creation the enabled property will be false
    }

    @ManyToMany(fetch = FetchType.EAGER)
    //We define this relationship of User model with Role
    //We define fetch because when we fetch the users we also need to fetch their roles
    private List<Role> roles = new ArrayList<>();
}
