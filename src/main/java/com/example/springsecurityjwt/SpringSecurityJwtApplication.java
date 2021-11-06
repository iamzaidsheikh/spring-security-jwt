package com.example.springsecurityjwt;

import java.util.ArrayList;

import com.example.springsecurityjwt.models.Role;
import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.services.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

	//Adding data to the database using command line runner
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			
			//Everything inside this block runs after the application has been initialized

			//Saving roles
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			//Saving users
			userService.saveUser(new User(null, "Charles Leclerc", "charles", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Max Verstappen", "max", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Sebastian Vettel", "seb", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Lewis Hamilton", "lewis", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Lando Norris", "lando", "1234", new ArrayList<>()));

			//Adding roles to users
			userService.addRoleToUser("charles", "ROLE_USER");
			userService.addRoleToUser("max", "ROLE_USER");
			userService.addRoleToUser("max", "ROLE_MANAGER");
			userService.addRoleToUser("lewis", "ROLE_USER");
			userService.addRoleToUser("lewis", "ROLE_ADMIN");
			userService.addRoleToUser("lewis", "ROLE_SUPER_ADMIN");

		};
	}

}
