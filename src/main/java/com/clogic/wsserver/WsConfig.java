package com.clogic.wsserver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer {
    private static List<String> sessions = new ArrayList<String>();

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/user/queue/specific-user","room");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/room")
            .addInterceptors(new HandshakeInterceptor() {
                @Override
                public boolean beforeHandshake(ServerHttpRequest request,
                    ServerHttpResponse response,
                    WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                    System.out.println("beforeHandshake called with headers " + request.getHeaders());
                    String body =  new String(request.getBody().readAllBytes(), StandardCharsets.UTF_8.name());
                    System.out.println("beforeHandshake called with body " + body);
                    System.out.println("beforeHandshake called with URI " + request.getURI());
                    System.out.println("beforeHandshake called with Principal " + request.getPrincipal());
                    if (request instanceof ServletServerHttpRequest) {
                      ServletServerHttpRequest servletRequest
                          = (ServletServerHttpRequest) request;
                      HttpSession session = servletRequest
                          .getServletRequest().getSession();
                      attributes.put("sessionId", session.getId());
                      System.out.println("beforeHandshake found session " + session.getId());
                      sessions.add(session.getId());
                    }
                    return true;
                }

                @Override
                public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                    WebSocketHandler wsHandler, Exception exception) {
                  try {
                    System.out.println("beforeHandshake called with headers " + response.getHeaders());
                    System.out.println("beforeHandshake called with body " + response.getBody());
                  } catch (IOException e) {  }
                }
            })
            .withSockJS();
    }
}
