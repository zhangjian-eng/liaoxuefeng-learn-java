package com.itranswarp.learnjava.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itranswarp.learnjava.messaging.LoginMessage;
import com.itranswarp.learnjava.messaging.RegistrationMessage;

@Component
public class MessagingService {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	public void sendRegistrationMessage(RegistrationMessage msg) throws IOException {
		send("topic_registration", msg);
	}

	public void sendLoginMessage(LoginMessage msg) throws IOException {
		send("topic_login", msg);
	}

	private void send(String topic, Object msg) throws IOException {
		ProducerRecord<String, String> pr = new ProducerRecord<>(topic, objectMapper.writeValueAsString(msg));
		pr.headers().add("type", msg.getClass().getName().getBytes(StandardCharsets.UTF_8));
		kafkaTemplate.send(pr);
	}
}
