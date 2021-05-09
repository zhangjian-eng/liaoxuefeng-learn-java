package com.itranswarp.learnjava.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.itranswarp.learnjava.dao.UserDao;

class UserServiceTest {

	UserService userService;

	@BeforeEach
	void setUp() throws Exception {
		this.userService = new UserService();
		this.userService.userDao = new UserDao() {
			@Override
			public User fetchUserByEmail(String email) {
				User user = new User();
				user.setEmail("test@example.com");
				user.setId(123);
				user.setName("Bob");
				user.setPassword("password");
				if (user.getEmail().equals(email)) {
					return user;
				}
				return null;
			}
		};
	}

	@Test
	void testLoginOk() {
		User user = this.userService.login("test@example.com", "password");
		assertNotNull(user);
	}

	@Test
	void testLoginFailed() {
		assertThrows(RuntimeException.class, () -> {
			this.userService.login("bad@example.com", "password");
		});
	}
}
