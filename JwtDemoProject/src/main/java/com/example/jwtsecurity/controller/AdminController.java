package com.example.jwtsecurity.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtsecurity.models.Role;
import com.example.jwtsecurity.models.User;
import com.example.jwtsecurity.service.UserAuthService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private UserAuthService uservice;
	
	
	@PostMapping("/add-role")
	public Role addRole(@RequestBody Role r) {
		return uservice.saveRole(r);
	}
	
	@GetMapping("/test")
	public String getTest() {
		return "Admin Test";
	}
	@GetMapping("/test1")
	public String getTest1() {
		return "Admin Test1";
	}
}
