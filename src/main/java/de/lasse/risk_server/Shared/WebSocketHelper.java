package de.lasse.risk_server.Shared;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketHelper {

    public static TextMessage generateTextMessage(String event, String data) {
        return new TextMessage("{\"event\":\"" + event + "\", \"data\":" + data + "}");
    }

    public static TextMessage generateTextMessage(String event) {
        return new TextMessage("{\"event\":\"" + event + "\"}");
    }

    public static <T> TextMessage generateTextMessage(String event, T obj) throws JsonProcessingException {
        String serializedObject = new ObjectMapper().writeValueAsString(obj);
        return generateTextMessage(event, serializedObject);
    }

    public static TextMessage generateDeclineMessage(String reason) {
        return generateTextMessage("declined", "{\"reason\":\"" + reason + "\"}");
    }
}
