package de.lasse.risk_server.Shared;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketHelper {

    private static TextMessage generateTextMessage(String event, String data) {
        return new TextMessage("{\"event\":\"" + event + "\", \"data\":" + data + "}");
    }

    public static TextMessage generateTextMessage(String event) {
        return new TextMessage("{\"event\":\"" + event + "\"}");
    }

    public static <T> TextMessage generateTextMessage(String event, T obj) {
        try {
            String serializedObject = new ObjectMapper().writeValueAsString(obj);
            return generateTextMessage(event, serializedObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static TextMessage generateGameActionMessage(String action) {
        return new TextMessage("{\"event\": gameaction, \"action\":\"" + action + "\"}");
    }

    private static TextMessage generateGameActionMessage(String action, String data) {
        return new TextMessage("{\"event\": gameaction, \"action\":\"" + action + "\", \"data\":" + data + "}");
    }

    public static <T> TextMessage generateGameActionMessage(String action, T obj) {
        try {
            String serializedObject = new ObjectMapper().writeValueAsString(obj);
            return generateGameActionMessage(action, serializedObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static TextMessage generateDeclineMessage(String reason) {
        return generateTextMessage("declined", "{\"reason\":\"" + reason + "\"}");
    }

    public static TextMessage generateForbidMessage() {
        return generateTextMessage("forbidden");

    }
}
