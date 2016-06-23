package com.cesi.rila.chat.web.websocket.event;

import java.util.Date;

public class OnlineEvent {
	private String username;
	private Date time;

	public OnlineEvent(String username) {
		this.username = username;
		time = new Date();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
