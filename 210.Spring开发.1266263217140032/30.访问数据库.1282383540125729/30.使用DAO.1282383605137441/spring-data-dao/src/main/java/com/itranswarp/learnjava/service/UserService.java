package com.itranswarp.learnjava.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itranswarp.learnjava.dao.UserDao;

@Component
@Transactional
public class UserService {

	@Autowired
	UserDao userDao;

	public User getUserById(long id) {
		return userDao.getById(id);
	}

	public User fetchUserByEmail(String email) {
		return userDao.fetchUserByEmail(email);
	}

	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	public List<User> getUsers(int pageIndex) {
		return userDao.getAll(pageIndex);
	}

	public User login(String email, String password) {
		User user = fetchUserByEmail(email);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		throw new RuntimeException("login failed.");
	}

	public User register(String email, String password, String name) {
		if (userDao.fetchUserByEmail(email) != null) {
			throw new RuntimeException("Email is alreay exist.");
		}
		return userDao.createUser(email, password, name);
	}

	public void updateUser(User user) {
		userDao.updateUser(user);
	}
}
