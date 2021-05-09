package com.itranswarp.learnjava.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itranswarp.learnjava.entity.User;

@Component
public class ChatHandler extends TextWebSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ChatHistory chatHistory;

	@Autowired
	ObjectMapper objectMapper;

	// 保存所有Client的WebSocket会话实例:
	private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

	public void broadcastMessage(ChatMessage chat) throws IOException {
		TextMessage message = toTextMessage(List.of(chat));
		for (String id : clients.keySet()) {
			WebSocketSession session = clients.get(id);
			session.sendMessage(message);
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String s = message.getPayload().strip();
		if (s.isEmpty()) {
			return;
		}
		String name = (String) session.getAttributes().get("name");
		ChatText chat = objectMapper.readValue(s, ChatText.class);
		var msg = new ChatMessage(name, chat.text);
		chatHistory.addToHistory(msg);
		broadcastMessage(msg);
	}

	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		clients.put(session.getId(), session);
		String name = null;
		User user = (User) session.getAttributes().get("__user__");
		if (user != null) {
			name = user.getName();
		} else {
			name = initGuestName();
		}
		session.getAttributes().put("name", name);
		logger.info("websocket connection established: id = {}, name = {}", session.getId(), name);
		// 把历史消息发给新用户:
		List<ChatMessage> list = chatHistory.getHistory();
		session.sendMessage(toTextMessage(list));
		// 添加系统消息并广播:
		var msg = new ChatMessage("SYSTEM MESSAGE", name + " joined the room.");
		chatHistory.addToHistory(msg);
		broadcastMessage(msg);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		clients.remove(session.getId());
		logger.info("websocket connection closed: id = {}, close-status = {}", session.getId(), status);
	}

	private TextMessage toTextMessage(List<ChatMessage> messages) throws IOException {
		String json = objectMapper.writeValueAsString(messages);
		return new TextMessage(json);
	}

	private String initGuestName() {
		return "Guest" + this.guestNumber.incrementAndGet();
	}

	private AtomicInteger guestNumber = new AtomicInteger();
}
