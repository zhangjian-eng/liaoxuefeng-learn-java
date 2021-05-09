package com.itranswarp.learnjava.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itranswarp.learnjava.entity.User;

@Component
@Transactional
public class UserService {

	@PersistenceContext
	EntityManager em;

	public User getUserById(long id) {
		User user = em.find(User.class, id);
		if (user == null) {
			throw new RuntimeException("User not found by id: " + id);
		}
		return user;
	}

	public User fetchUserByEmail(String email) {
		var cb = em.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> r = q.from(User.class);
		q.where(cb.equal(r.get("email"), cb.parameter(String.class, "e")));
		TypedQuery<User> query = em.createQuery(q);
		query.setParameter("e", email);
		List<User> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	public User getUserByEmail(String email) {
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :e", User.class);
		query.setParameter("e", email);
		List<User> list = query.getResultList();
		if (list.isEmpty()) {
			throw new RuntimeException("User not found by email.");
		}
		return list.get(0);
	}

	public List<User> getUsers(int pageIndex) {
		int pageSize = 100;
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
		query.setFirstResult((pageIndex - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public User login(String email, String password) {
		TypedQuery<User> query = em.createNamedQuery("login", User.class);
		query.setParameter("e", email);
		query.setParameter("p", password);
		List<User> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	public User register(String email, String password, String name) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		em.persist(user);
		return user;
	}

	public void updateUser(Long id, String name) {
		User user = getUserById(id);
		user.setName(name);
		em.refresh(user);
	}

	public void deleteUser(Long id) {
		User user = getUserById(id);
		em.remove(user);
	}
}
