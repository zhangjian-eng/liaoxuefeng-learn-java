package com.itranswarp.learnjava.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;

@Order(10)
@Component
public class AuthFilterRegistrationBean extends FilterRegistrationBean<Filter> {

	@Autowired
	UserService userService;

	@Override
	public Filter getFilter() {
		return new AuthFilter();
	}

	class AuthFilter implements Filter {

		final Logger logger = LoggerFactory.getLogger(getClass());

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			HttpServletRequest req = (HttpServletRequest) request;
			try {
				authenticateByHeader(req);
			} catch (RuntimeException e) {
				logger.warn("login by authorization header failed.", e);
			}
			chain.doFilter(request, response);
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
}
