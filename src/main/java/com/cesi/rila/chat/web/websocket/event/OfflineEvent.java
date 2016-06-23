package com.cesi.rila.chat.web.websocket.event;

public class OfflineEvent {
	private String username;

	public OfflineEvent(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
