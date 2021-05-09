package com.itranswarp.learnjava.mail;

public interface MailService {

	void sendMail(String address, String subject, String body);
}
