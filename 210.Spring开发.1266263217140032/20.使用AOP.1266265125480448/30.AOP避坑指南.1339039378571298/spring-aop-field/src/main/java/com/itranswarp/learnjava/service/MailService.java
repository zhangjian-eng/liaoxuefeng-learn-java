package com.itranswarp.learnjava.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailService {

	@Autowired
	UserService userService;

	public String sendMail() {
		// null:
		ZoneId zoneId = userService.zoneId;
		String dt = ZonedDateTime.now(zoneId).toString();
		return "Hello, it is " + dt;
	}
}
