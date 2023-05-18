package de.lasse.risk_server.Lobby;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;

public class WebSocketHelper {

    public static TextMessage generateTextMessage(String event, JSONObject data) {
        JSONObject out = new JSONObject();
        out.put("data", data);
        out.put("event", event);
        return new TextMessage(out.toString());
    }

    public static TextMessage generateDeclineMessage(String reason) {
        return generateTextMessage("declined", new JSONObject("{'reason':'" + reason + "'}"));
    }
}
