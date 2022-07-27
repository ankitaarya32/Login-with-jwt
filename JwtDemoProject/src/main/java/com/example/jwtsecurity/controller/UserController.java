package com.example.jwtsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@GetMapping("/test")
	public String getTest() {
		return "User Test";
	}
	@GetMapping("/test1")
	public String getTest1() {
		return "User Test1";
	}
}
