package com.taskboard.collabboard.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;

@Component
public class WebSocketEventListener {

  private final SimpMessagingTemplate messagingTemplate;
  private final StringRedisTemplate redisTemplate;
  private final String ONLINE_USERS_KEY = "board:online_users";

  public WebSocketEventListener(SimpMessagingTemplate messagingTemplate, StringRedisTemplate redisTemplate) {
    this.messagingTemplate = messagingTemplate;
    this.redisTemplate = redisTemplate;
  }

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    if (event.getUser() != null) {
      String username = event.getUser().getName();
      redisTemplate.opsForSet().add(ONLINE_USERS_KEY, username);
      broadcastOnlineUsers();
    }
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    if (event.getUser() != null) {
      String username = event.getUser().getName();
      redisTemplate.opsForSet().remove(ONLINE_USERS_KEY, username);
      broadcastOnlineUsers();
    }
  }

  private void broadcastOnlineUsers() {
    Set<String> onlineUsers = redisTemplate.opsForSet().members(ONLINE_USERS_KEY);
    messagingTemplate.convertAndSend("/topic/online-users", onlineUsers);
  }
}
