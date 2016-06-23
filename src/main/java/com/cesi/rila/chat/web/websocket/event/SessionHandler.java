package com.cesi.rila.chat.web.websocket.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler {

	private Map<String, OnlineEvent> activeSessions = new ConcurrentHashMap<>();
	
	public void add(String sessionId, OnlineEvent event) {
		activeSessions.put(sessionId, event);
	}

	public OnlineEvent getParticipant(String sessionId) {
		return activeSessions.get(sessionId);
	}

	public void removeParticipant(String sessionId) {
		activeSessions.remove(sessionId);
	}

	public Map<String, OnlineEvent> getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(Map<String, OnlineEvent> activeSessions) {
		this.activeSessions = activeSessions;
	}
	
}
