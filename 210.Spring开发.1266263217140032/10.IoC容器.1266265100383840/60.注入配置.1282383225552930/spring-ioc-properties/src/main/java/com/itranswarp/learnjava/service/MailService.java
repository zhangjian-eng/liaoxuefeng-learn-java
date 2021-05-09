package com.itranswarp.learnjava.service;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailService {

	@Value("#{smtpConfig.host}")
	private String smtpHost;

	@Value("#{smtpConfig.port}")
	private int smtpPort;

	@Autowired
	private ZoneId zoneId;

	public void sendWelcomeMail(User user) {
		System.out.println("at zone: " + zoneId);
		System.out.println("sent by smtp host: " + smtpHost);
		System.out.println("sent by smtp port: " + smtpPort);
	}
}
