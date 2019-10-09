package be.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class VolumeController extends AbstractWebSocketHandler {

    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        if(message.getPayload().equals("open")){
            System.out.println("added " + session.getId());
            sessions.add(session);
        }
        sendMessageToAll(message);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("New Binary Message Received");
        session.sendMessage(message);
    }

    private void sendMessageToAll(TextMessage message) {
        for (WebSocketSession session : sessions) {
            try {
                if(session.isOpen()){
                    session.sendMessage(message);
                }
            } catch (IOException ex) {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}