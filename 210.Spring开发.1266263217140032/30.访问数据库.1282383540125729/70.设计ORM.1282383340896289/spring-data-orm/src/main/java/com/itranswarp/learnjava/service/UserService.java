package com.itranswarp.learnjava.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.orm.DbTemplate;

@Component
@Transactional
public class UserService {

	@Autowired
	DbTemplate db;

	public User getUserById(long id) {
		return db.get(User.class, id);
	}

	public User fetchUserByEmail(String email) {
		return db.from(User.class).where("email = ?", email).first();
	}

	public User getUserByEmail(String email) {
		return db.from(User.class).where("email = ?", email).unique();
	}

	public String getNameByEmail(String email) {
		User user = db.select("name").from(User.class).where("email = ?", email).unique();
		return user.getName();
	}

	public List<User> getUsers(int pageIndex) {
		int pageSize = 100;
		return db.from(User.class).orderBy("id").limit((pageIndex - 1) * pageSize, pageSize).list();
	}

	public User login(String email, String password) {
		User user = fetchUserByEmail(email);
		if (user != null && password.equals(user.getPassword())) {
			return user;
		}
		throw new RuntimeException("login failed.");
	}

	public User register(String email, String password, String name) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		user.setCreatedAt(System.currentTimeMillis());
		db.insert(user);
		return user;
	}

	public void updateUser(Long id, String name) {
		User user = getUserById(id);
		user.setName(name);
		user.setCreatedAt(System.currentTimeMillis());
		db.update(user);
	}

	public void deleteUser(Long id) {
		db.delete(User.class, id);
	}
}
