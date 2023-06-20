package de.lasse.risk_server.Lobby;

import org.springframework.web.socket.WebSocketSession;

public class QueryIdentification {

    public String event, lobbyId, token;
    public WebSocketSession session;

    public QueryIdentification(String event, String lobbyId, String token, WebSocketSession session) {
        this.event = event;
        this.lobbyId = lobbyId;
        this.token = token;
        this.session = session;
    }

}
