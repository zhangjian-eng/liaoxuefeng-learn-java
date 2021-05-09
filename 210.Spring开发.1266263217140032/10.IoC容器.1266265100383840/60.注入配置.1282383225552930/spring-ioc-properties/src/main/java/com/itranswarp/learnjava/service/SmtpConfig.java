package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmtpConfig {

	@Value("${smtp.host:localhost}")
	private String host;

	@Value("${smtp.port:25}")
	private int port;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}
