package de.lasse.risk_server.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GamesHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username = (String) session.getAttributes().get("username");
        if (username == null) {
            session.getAttributes().put("username", message.getPayload());
            session.sendMessage(new TextMessage("You have joined the chat."));
        } else {
            String text = message.getPayload();
            String response = String.format("%s: %s", username, text);
            broadcast(new TextMessage(response));
        }
    }

    private void broadcast(TextMessage message) throws IOException {
        for (WebSocketSession session : sessions) {
            session.sendMessage(message);
        }
    }

}
