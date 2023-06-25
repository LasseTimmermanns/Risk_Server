package de.lasse.risk_server.Shared;

import org.springframework.web.socket.WebSocketSession;

public class QueryIdentification {

    public String event, roomId, token;
    public WebSocketSession session;

    public QueryIdentification(String event, String roomId, String token, WebSocketSession session) {
        this.event = event;
        this.roomId = roomId;
        this.token = token;
        this.session = session;
    }

}
