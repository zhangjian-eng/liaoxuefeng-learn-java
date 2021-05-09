package com.itranswarp.learnjava.service;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itranswarp.learnjava.web.MailMessage;

@Component
public class MailMessageListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MailService mailService;

	@JmsListener(destination = "jms/queue/mail", concurrency = "10")
	public void onMailMessageReceived(Message message) throws Exception {
		logger.info("received message: " + message);
		if (message instanceof TextMessage) {
			String text = ((TextMessage) message).getText();
			MailMessage mm = objectMapper.readValue(text, MailMessage.class);
			mailService.sendRegistrationMail(mm);
		} else {
			logger.error("unable to process non-text message!");
		}
	}
}
