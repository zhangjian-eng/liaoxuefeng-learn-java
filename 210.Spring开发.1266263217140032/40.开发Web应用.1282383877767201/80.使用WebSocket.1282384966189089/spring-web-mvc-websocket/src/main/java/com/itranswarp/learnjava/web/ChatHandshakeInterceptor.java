package com.itranswarp.learnjava.web;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	public ChatHandshakeInterceptor() {
		// 指定从HttpSession复制属性到WebSocketSession:
		super(List.of(UserController.KEY_USER));
	}
}
