package com.itranswarp.learnjava;

import java.time.ZoneId;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.itranswarp.learnjava.service.UserService;

@Configuration
@ComponentScan
public class AppConfig {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		userService.login("bob@example.com", "password");
	}

	@Bean
	@Profile("!test")
	ZoneId createZoneId() {
		return ZoneId.systemDefault();
	}

	@Bean
	@Profile("test")
	ZoneId createZoneIdForTest() {
		return ZoneId.of("America/New_York");
	}
}
