package com.itranswarp.learnjava.web;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;

@Order(2)
@Component
public class AuthInterceptor implements HandlerInterceptor {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("pre authenticate {}...", request.getRequestURI());
		try {
			authenticateByHeader(request);
		} catch (RuntimeException e) {
			logger.warn("login by authorization header failed.", e);
		}
		return true;
	}

	private void authenticateByHeader(HttpServletRequest req) {
		String authHeader = req.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Basic ")) {
			logger.info("try authenticate by authorization header...");
			String up = new String(Base64.getDecoder().decode(authHeader.substring(6)), StandardCharsets.UTF_8);
			int pos = up.indexOf(':');
			if (pos > 0) {
				String email = URLDecoder.decode(up.substring(0, pos), StandardCharsets.UTF_8);
				String password = URLDecoder.decode(up.substring(pos + 1), StandardCharsets.UTF_8);
				User user = userService.signin(email, password);
				req.getSession().setAttribute(UserController.KEY_USER, user);
				logger.info("user {} login by authorization header ok.", email);
			}
		}
	}
}
