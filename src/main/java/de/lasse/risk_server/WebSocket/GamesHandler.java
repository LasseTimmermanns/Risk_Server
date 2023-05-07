package de.lasse.risk_server.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
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
            // session.sendMessage(new TextMessage("You have joined the chat."));
            session.sendMessage(message);
        } else {
            String text = message.getPayload();
            JSONObject msg = new JSONObject();
            msg.put("action", "move");
            msg.put("from", 0);
            msg.put("to", 1);
            msg.put("msg", text);
            broadcast(msg);
        }
    }

    private void broadcast(JSONObject json) throws IOException {
        TextMessage message = new TextMessage(json.toString());
        for (WebSocketSession session : sessions) {
            session.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
