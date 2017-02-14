package com.movienius.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.movienius.models.User;
import com.movienius.models.UserRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRep;
	
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
	public Iterable<User> usersList(){
		return this.userRep.findAll();
	}
	
	@RequestMapping(value = "/users/{name}", method = RequestMethod.GET, produces = "application/json")
	public User showUser(@PathVariable String name){
		return this.userRep.findByName(name);
	}
	
	@RequestMapping(value = "/users/{name}", method = RequestMethod.PUT, produces = "application/json")
	public User putUser(@PathVariable String name,
						@RequestParam(value = "name", required=false) String newName,
						@RequestParam(value = "password", required=false) String password,
						@RequestParam(value = "email", required=false) String email,
						@RequestParam(value = "admin", required=false) Boolean admin){
		User user = this.userRep.findByName(name);
		if (user == null) throw new UserNotFoundException(name);
		if(newName != null && !newName.isEmpty()) user.setName(newName);
		if(password != null && !password.isEmpty()) user.setPassword(password);
		if(email != null) user.setEmail(email);
		if(admin != null){
			ArrayList<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
			roles.add(new SimpleGrantedAuthority("ROLE_USER"));
			if (admin)
				roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			user.setRoles(roles);
		}
		this.userRep.save(user);
		return user;
	}
	
	@RequestMapping(value = "/users/{name}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteUser(@PathVariable String name){
		User user = this.userRep.findByName(name);
		if (user == null) throw new UserNotFoundException(name);
		this.userRep.delete(user);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public User addUser(@RequestParam("name") String name,
						@RequestParam("password") String password,
						@RequestParam("email") String email,
						@RequestParam("admin") boolean admin){
		if (this.userRep.findByName(name) != null) throw new UserConflictException(name);
		ArrayList<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (admin)
			roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		User user = new User(name, password, email, roles);
		this.userRep.save(user);
		return user;
	}
}
