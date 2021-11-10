package com.example.springsecurityjwt;

import java.util.ArrayList;

import com.example.springsecurityjwt.models.Role;
import com.example.springsecurityjwt.models.User;
import com.example.springsecurityjwt.services.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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

			//Registering users
			userService.registerUser(new User(null, "Charles", "Leclerc", "charles", "1234", "charlesLeclerc@scuderiaferrari.com", true, new ArrayList<>()));
			userService.registerUser(new User(null, "Max", "Verstappen", "max", "1234", "maxVerstappen@redbullracing.com", true, new ArrayList<>()));
			userService.registerUser(new User(null, "Sebastian", "Vettel", "seb", "1234", "sebVettel@astonmartinracing.com", true, new ArrayList<>()));
			userService.registerUser(new User(null, "Lewis", "Hamilton", "lewis", "1234", "lewisHamilton@mercedesamgracing.com", true, new ArrayList<>()));

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
