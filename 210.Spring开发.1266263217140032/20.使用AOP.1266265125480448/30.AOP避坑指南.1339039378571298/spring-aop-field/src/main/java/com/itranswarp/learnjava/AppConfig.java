package com.itranswarp.learnjava;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.itranswarp.learnjava.service.MailService;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		MailService mailService = context.getBean(MailService.class);
		// NullPointerException:
		System.out.println(mailService.sendMail());
	}
}
