package com.itranswarp.learnjava.service;

import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.itranswarp.learnjava.entity.User;

@Component
public class MailService {

	@Value("${smtp.from}")
	String from;

	@Autowired
	JavaMailSender mailSender;

	public void sendRegistrationMail(User user) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setFrom(from);
			helper.setTo(user.getEmail());
			helper.setSubject("Welcome to Java course!");
			String html = String.format("<p>Hi, %s,</p><p>Welcome to Java course!</p><p>Sent at %s</p>", user.getName(),
					LocalDateTime.now());
			helper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
