package com.itranswarp.learnjava.web;

public class MailMessage {

	public static enum Type {
		REGISTRATION, SIGNIN;
	}

	public Type type;
	public String email;
	public String name;
	public long timestamp;

	public static MailMessage registration(String email, String name) {
		var msg = new MailMessage();
		msg.email = email;
		msg.name = name;
		msg.type = Type.REGISTRATION;
		msg.timestamp = System.currentTimeMillis();
		return msg;
	}
}
