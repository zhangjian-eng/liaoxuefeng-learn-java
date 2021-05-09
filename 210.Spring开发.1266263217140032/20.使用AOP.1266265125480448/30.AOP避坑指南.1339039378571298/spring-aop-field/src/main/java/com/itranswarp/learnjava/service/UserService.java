package com.itranswarp.learnjava.service;

import java.time.ZoneId;

import org.springframework.stereotype.Component;

@Component
public class UserService {

	public final ZoneId zoneId = ZoneId.systemDefault();

	public UserService() {
		System.out.println("UserService(): init...");
		System.out.println("UserService(): zoneId = " + this.zoneId);
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public final ZoneId getFinalZoneId() {
		return zoneId;
	}
}
