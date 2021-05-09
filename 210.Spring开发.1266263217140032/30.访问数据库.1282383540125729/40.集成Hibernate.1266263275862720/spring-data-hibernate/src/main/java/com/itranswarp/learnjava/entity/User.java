package com.itranswarp.learnjava.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries(@NamedQuery(name = "login", query = "SELECT u FROM User u WHERE u.email=?0 AND u.password=?1"))
@Entity
public class User extends AbstractEntity {

	private String email;
	private String password;
	private String name;

	public User() {
	}

	public User(long id, String email, String password, String name) {
		setId(id);
		setEmail(email);
		setPassword(password);
		setName(name);
	}

	@Column(nullable = false, unique = true, length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false, length = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(),
				getEmail(), getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
	}
}
