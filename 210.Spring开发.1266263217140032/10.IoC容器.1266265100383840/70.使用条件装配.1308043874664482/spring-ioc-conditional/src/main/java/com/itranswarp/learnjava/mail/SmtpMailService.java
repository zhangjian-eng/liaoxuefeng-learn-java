package com.itranswarp.learnjava.mail;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(OnSmtpEnvCondition.class)
public class SmtpMailService implements MailService {

	@Override
	public void sendMail(String address, String subject, String body) {
		System.out.println("Send mail to " + address + " using SMTP:");
		System.out.println("Subject: " + subject);
		System.out.println("Body: " + body);

	}
}
