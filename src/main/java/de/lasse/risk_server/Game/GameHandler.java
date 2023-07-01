package de.lasse.risk_server.Game;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.lasse.risk_server.Game.Game.GameInterfaceRepository;
import de.lasse.risk_server.Game.Utils.JoinManagement;
import de.lasse.risk_server.Shared.QueryIdentification;

@Service
public class GameHandler extends TextWebSocketHandler {

    @Autowired
    GameInterfaceRepository gameInterfaceRepository;

    @Autowired
    JoinManagement joinManagement;

    public static HashMap<String, List<WebSocketSession>> sessions = new HashMap<String, List<WebSocketSession>>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected WebSocket");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        JsonNode message_json = new ObjectMapper().readTree(message.getPayload());

        JsonNode data = message_json.has("data") ? message_json.get("data") : null;
        QueryIdentification queryIdentification = null;

        try {
            queryIdentification = new QueryIdentification(message_json.get("event").asText(),
                    data.get("gameId").asText(), data.get("token").asText(), session);
        } catch (NullPointerException e) {
        }

        try {
            switch (message_json.get("event").asText()) {
                case "join":
                    joinManagement.join(queryIdentification, session);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }
}
