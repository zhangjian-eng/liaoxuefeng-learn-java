package com.itranswarp.learnjava.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itranswarp.learnjava.web.MailMessage;

@Component
public class MessagingService {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	JmsTemplate jmsTemplate;

	public void sendMailMessage(MailMessage msg) throws Exception {
		String text = objectMapper.writeValueAsString(msg);
		jmsTemplate.send("jms/queue/mail", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(text);
			}
		});
	}
}
