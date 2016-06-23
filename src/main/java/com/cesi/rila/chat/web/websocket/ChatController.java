package com.cesi.rila.chat.web.websocket;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.cesi.rila.chat.domain.ChatMessage;
import com.cesi.rila.chat.web.websocket.event.OnlineEvent;
import com.cesi.rila.chat.web.websocket.event.SessionHandler;

@Controller
public class ChatController {
	@Autowired private SessionHandler participantRepository;
	
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	
	@SubscribeMapping("/chat/participants")
	public Collection<OnlineEvent> retrieveParticipants() {
		return participantRepository.getActiveSessions().values();
	}
	
	@MessageMapping("/chat/message")
	@SendTo("/topic/chat/message")
	public ChatMessage filterMessage(@Payload ChatMessage message, Principal principal) {
		
		message.setUsername(principal.getName());
		
		return message;
	}
	
	@MessageMapping("/chat/private/{username}")
	@SendTo("/topic/chat/private/{username}")
	public ChatMessage filterPrivateMessage(@Payload ChatMessage message, @DestinationVariable("username") String username, Principal principal) {
		
		message.setUsername(principal.getName());
		return message;
	}
}
