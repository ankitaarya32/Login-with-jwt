package com.example.jwtsecurity.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.example.jwtsecurity.models.Role;
import com.example.jwtsecurity.models.User;
import com.example.jwtsecurity.repo.UserRepo;
import com.example.jwtsecurity.service.UserAuthService;

@RestController
@RequestMapping("/api/public")
public class PublicRestController {
	@Autowired
	private UserRepo urepo;
	@Autowired
	private UserAuthService uservice;

	@GetMapping("/")
	public String getWord() {
		//uservice.saveRole(new Role("SUPERADMIN","Have all privileges"));
		Set<Role> s1=new HashSet<Role>();
		s1.add(uservice.findRoleByRoleName("ADMIN"));
		
		//User u=new User("Ankit","$2a$12$GBj8W/Snhl/55yg04UfaHeXbU4DbJZ7Y.66QGGd2y31CSjqtMYvKO",s1);
		uservice.adminsignup("Ankit","$2a$12$GBj8W/Snhl/55yg04UfaHeXbU4DbJZ7Y.66QGGd2y31CSjqtMYvKO",s1);
		return "Hello World!!";
	}

	@PostMapping("/users")
	public User addUser(@RequestBody User user) {
		return urepo.save(user);
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return urepo.findAll();
	}
	
	@GetMapping("/users/adduser")
	public String addTestUser() {
		Set<Role> s1=new HashSet<Role>();
		User u=new User("Ankit","$2a$12$GBj8W/Snhl/55yg04UfaHeXbU4DbJZ7Y.66QGGd2y31CSjqtMYvKO",s1);
		urepo.save(u);
		 u=new User("Ankit1","test",s1);
		urepo.save(u);
		return "user added";
	}

	@GetMapping("/admin/{id}")
	public User getUsers(@PathVariable int id) {
		return urepo.findById(id).get();
	}
	
	@PostMapping("/login")
	public Authentication login(@RequestBody User u) {
		return uservice.login(u.getUsername(),u.getPassword());
	}
	
	@PostMapping("/signin")
	public Map<String ,String> signIn(@RequestBody User u,HttpServletResponse res) {
		//return uservice.signin(u.getUsername(), u.getPassword()).orElseThrow(()->new HttpServerErrorException(HttpStatus.FORBIDDEN,"Login Failed"));
	return uservice.signin(u.getUsername(), u.getPassword(),res);
	}
	
	@GetMapping("/signin-browser")
	public Map<String ,String> signInViaBrowser(@RequestParam String username,String password,HttpServletResponse res) {
		//return uservice.signin(u.getUsername(), u.getPassword()).orElseThrow(()->new HttpServerErrorException(HttpStatus.FORBIDDEN,"Login Failed"));
	return uservice.signin(username, password,res);
	}
	@PostMapping("/signup")
	public Optional<User> signup(@RequestBody User u) {
		System.out.println("User Details By user - "+u);
		//if(uservice.loadUserByUsername(u.getUsername())!= null) return Optional.empty();
		//else
		return uservice.signup(u.getUsername(),u.getPassword());
	}
	@PostMapping("/signup-admin")
	public Optional<User> signupAdmin(@RequestBody User u) {
		return uservice.adminsignup(u.getUsername(),u.getPassword(),u.getRoles());
	}
	
	@PostMapping("/get-user")
	public Optional<User> getUserByToken(@RequestBody String token){
		return uservice.loadUserByToken(token);
	}
	
	
}
