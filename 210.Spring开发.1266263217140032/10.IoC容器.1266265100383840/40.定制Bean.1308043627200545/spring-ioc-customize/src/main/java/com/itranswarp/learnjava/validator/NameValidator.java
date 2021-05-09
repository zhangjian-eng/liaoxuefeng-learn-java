package com.itranswarp.learnjava.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class NameValidator implements Validator {

	@Override
	public void validate(String email, String password, String name) {
		if (name == null || name.isBlank() || name.length() > 20) {
			throw new IllegalArgumentException("invalid name: " + name);
		}
	}
}
