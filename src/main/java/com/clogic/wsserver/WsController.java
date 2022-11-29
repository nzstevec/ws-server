package com.clogic.wsserver;

import com.google.gson.Gson;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WsController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private Gson gson = new Gson();

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageDto message) {
        System.out.println("sendMessage called with " + message);
        simpMessagingTemplate.convertAndSend("/room",message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @MessageMapping("/message")
//    @SendToUser("/queue/reply")
//    public String processMessageFromClient(
//        @Payload String message,
//        Principal principal) throws Exception {
//        return gson
//            .fromJson(message, Map.class)
//            .get("name").toString();
//    }

    @MessageMapping("/room")
    public void sendSpecific(
        @Payload MessageDto msg,
        Principal user,
        @Header("simpSessionId") String sessionId) throws Exception {
        System.out.println("sendSpecific called");
        OutputMessage out = new OutputMessage(
            msg.getFrom(),
            msg.getText(),
            new SimpleDateFormat("HH:mm").format(new Date()));
        simpMessagingTemplate.convertAndSendToUser(
            msg.getTo(), "/user/queue/specific-user", out);
    }


//    @SendTo("/topic/messages")
//    public MessageDto broadcastMessage(@Payload MessageDto message) {
//        return message;
//    }
}
