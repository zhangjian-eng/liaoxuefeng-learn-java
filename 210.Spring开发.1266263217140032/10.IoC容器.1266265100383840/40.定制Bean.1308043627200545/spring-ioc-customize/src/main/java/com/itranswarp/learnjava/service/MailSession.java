package com.itranswarp.learnjava.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MailSession {

	public MailSession() {
		System.out.println("new MailSession()");
	}
}
