package com.movienius;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.movienius.models.User;
import com.movienius.models.UserRepository;


@Component
public class DatabaseLoader {
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	private void initDatabase() {
		if(userRepository.findByName("admin") != null) return;
		// User #1: "user", with password "p1" and role "USER"
		GrantedAuthority[] userRoles = { new SimpleGrantedAuthority("ROLE_USER") };
		userRepository.save(new User("user", "p1", "p1@sd.sd", Arrays.asList(userRoles)));
		// User #2: "root", with password "p2" and roles "USER" and "ADMIN"
		GrantedAuthority[] adminRoles = { new SimpleGrantedAuthority("ROLE_USER"),
				new SimpleGrantedAuthority("ROLE_ADMIN") };
		userRepository.save(new User("admin", "admin", "admin@admin.com", Arrays.asList(adminRoles)));
	}
}