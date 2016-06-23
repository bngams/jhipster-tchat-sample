package com.cesi.rila.chat.config;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.cesi.rila.chat.web.websocket.event.OfflineEvent;
import com.cesi.rila.chat.web.websocket.event.OnlineEvent;
import com.cesi.rila.chat.web.websocket.event.SessionHandler;

@Configuration
public class WebsocketSessionConfiguration {
	private static final String LOGIN_DESTINATION = "/topic/online/connect";// connect
	private static final String LOGOUT_DESTINATION = "/topic/online/disconnect"; // disconnect
	
	// @Bean
	// private SessionHandler sessionHandler;

	@Inject
	private SimpMessagingTemplate messagingTemplate;
	
	@Bean
	public SessionHandler sessionHandler() {
		return new SessionHandler();
	}

	@EventListener
	private void handleSessionConnected(SessionConnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String username = headers.getUser().getName();

		OnlineEvent OnlineEvent = new OnlineEvent(username);
		messagingTemplate.convertAndSend(LOGIN_DESTINATION, OnlineEvent);
		
		// We store the session as we need to be idempotent in the disconnect event processing
		sessionHandler().add(headers.getSessionId(), OnlineEvent);
	}
	
	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		
		Optional.ofNullable(sessionHandler().getParticipant(event.getSessionId()))
				.ifPresent(login -> {
					messagingTemplate.convertAndSend(LOGOUT_DESTINATION, new OfflineEvent(login.getUsername()));
					sessionHandler().removeParticipant(event.getSessionId());
				});
	}
}
