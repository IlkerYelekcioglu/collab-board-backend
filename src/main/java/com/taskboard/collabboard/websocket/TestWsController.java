package com.taskboard.collabboard.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TestWsController {

  @MessageMapping("/test")
  @SendTo("/topic/test")
  public String handleTest(String message) {
    return "Sunucu yanıtı: " + message;
  }
}
