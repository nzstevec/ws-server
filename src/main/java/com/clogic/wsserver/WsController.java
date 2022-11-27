package com.clogic.wsserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WsController {
    @Autowired
    SimpMessagingTemplate template;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageDto message) {
        System.out.println("sendMessage called with " + message);
        template.convertAndSend("/topic/messages",message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @MessageMapping("/ws-message")
    @SendTo("/topic/messages")
    public OutputMessage send(final MessageDto message) {
        System.out.println("send() called with " + message);
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

    @SendTo("/topic/messages")
    public MessageDto broadcastMessage(@Payload MessageDto message) {
        return message;
    }
}
