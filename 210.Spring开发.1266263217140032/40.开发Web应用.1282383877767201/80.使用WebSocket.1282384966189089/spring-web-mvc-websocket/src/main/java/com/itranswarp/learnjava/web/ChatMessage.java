package com.itranswarp.learnjava.web;

public class ChatMessage extends ChatText {

	public long timestamp;
	public String name;

	public ChatMessage() {
	}

	public ChatMessage(String name, String text) {
		this.timestamp = System.currentTimeMillis();
		this.name = name;
		this.text = text;
	}
}

class ChatText {
	public String text;
}
