package com.itranswarp.learnjava.web;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;

	@GetMapping("/version")
	public Map<String, String> version() {
		logger.info("get version...");
		return Map.of("version", "1.0");
	}

	@GetMapping("/users")
	public Callable<List<User>> users() {
		logger.info("get users...");
		return () -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			logger.info("return users...");
			return userService.getUsers();
		};
	}

	@GetMapping("/users/{id}")
	public DeferredResult<User> user(@PathVariable("id") long id) {
		DeferredResult<User> result = new DeferredResult<>(3000L);
		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			try {
				User user = userService.getUserById(id);
				result.setResult(user);
				logger.info("deferred result is set.");
			} catch (Exception e) {
				result.setErrorResult(Map.of("error", e.getClass().getSimpleName(), "message", e.getMessage()));
				logger.warn("deferred error result is set.");
			}
		}).start();
		return result;
	}
}
