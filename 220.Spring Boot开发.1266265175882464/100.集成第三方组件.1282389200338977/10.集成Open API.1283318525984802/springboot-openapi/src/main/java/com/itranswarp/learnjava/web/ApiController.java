package com.itranswarp.learnjava.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	UserService userService;

	@GetMapping("/users")
	public List<User> users() {
		return userService.getUsers();
	}

	@Operation(summary = "Get specific user object by it's id.")
	@GetMapping("/users/{id}")
	public User user(@Parameter(description = "id of the user.") @PathVariable("id") long id) {
		return userService.getUserById(id);
	}

	@PostMapping("/signin")
	public Map<String, Object> signin(@RequestBody SignInRequest signinRequest) {
		try {
			User user = userService.signin(signinRequest.email, signinRequest.password);
			return Map.of("user", user);
		} catch (Exception e) {
			return Map.of("error", "SIGNIN_FAILED", "message", e.getMessage());
		}
	}

	public static class SignInRequest {
		public String email;
		public String password;
	}
}
