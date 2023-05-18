package de.lasse.risk_server.Lobby;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class MessageBroadcastTuple {
    public TextMessage message;
    public WebSocketSession session;
    public String lobbyid;

    public MessageBroadcastTuple(TextMessage message, String lobbyid) {
        this.message = message;
        this.lobbyid = lobbyid;
    }

    public MessageBroadcastTuple(TextMessage message, WebSocketSession session) {
        this.message = message;
        this.session = session;
    }

    public void execute() throws IOException {
        if (session == null) {
            executeBroadcast();
            return;
        }

        executeMessage();
    }

    private void executeBroadcast() throws IOException {
        for (WebSocketSession session : LobbyHandler.sessions.get(lobbyid)) {
            session.sendMessage(message);
        }
    }

    private void executeMessage() throws IOException {
        session.sendMessage(message);
    }

}
