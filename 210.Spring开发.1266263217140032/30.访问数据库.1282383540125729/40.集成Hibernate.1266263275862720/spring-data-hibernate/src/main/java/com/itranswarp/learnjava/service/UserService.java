package com.itranswarp.learnjava.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itranswarp.learnjava.entity.User;

@Component
@Transactional
public class UserService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	public User getUserById(long id) {
		return hibernateTemplate.load(User.class, id);
	}

	public User fetchUserByEmail(String email) {
		User example = new User();
		example.setEmail(email);
		List<User> list = hibernateTemplate.findByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}

	public User getUserByEmail(String email) {
		User user = fetchUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("user not found by email: " + email);
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers(int pageIndex) {
		int pageSize = 100;
		return (List<User>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(User.class),
				(pageIndex - 1) * pageSize, pageSize);
	}

	public User signin(String email, String password) {
		@SuppressWarnings({ "deprecation", "unchecked" })
		List<User> list = (List<User>) hibernateTemplate.find("FROM User WHERE email=? AND password=?", email,
				password);
		return list.isEmpty() ? null : list.get(0);
	}

	public User login(String email, String password) {
		@SuppressWarnings({ "deprecation", "unchecked" })
		List<User> list = (List<User>) hibernateTemplate.findByNamedQuery("login", email, password);
		return list.isEmpty() ? null : list.get(0);
	}

	public User register(String email, String password, String name) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		hibernateTemplate.save(user);
		return user;
	}

	public void updateUser(Long id, String name) {
		User user = hibernateTemplate.load(User.class, id);
		user.setName(name);
		hibernateTemplate.update(user);
	}

	public boolean deleteUser(Long id) {
		User user = hibernateTemplate.get(User.class, id);
		if (user != null) {
			hibernateTemplate.delete(user);
			return true;
		}
		return false;
	}
}
