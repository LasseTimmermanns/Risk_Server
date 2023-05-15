package de.lasse.risk_server.Lobby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.yaml.snakeyaml.util.ArrayUtils;

import de.lasse.risk_server.Database.Lobby.Color;
import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;
import de.lasse.risk_server.Utils.QueryUtil;
import de.lasse.risk_server.Utils.TokenGenerator;

@Service
public class LobbyHandler extends TextWebSocketHandler {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    private final HashMap<String, List<WebSocketSession>> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String lobbyid = QueryUtil.getQueryValue("lobby", session.getUri().getQuery());
        String playername = QueryUtil.getQueryValue("playername", session.getUri().getQuery());

        if (lobbyid == null || playername == null) {
            session.close();
            return;
        }

        Lobby lobby = lobbyInterfaceRepository.findById(lobbyid).orElse(null);
        if (lobby == null) {
            session.close();
            return;
        }

        if (lobby.maxPlayers >= lobby.players.length) {
            session.close();
            return;
        }

        // sessions.get(lobbyid).add(session);

        int position = lobby.players.length;
        String token = TokenGenerator.generateToken();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(playername, token, new Color("white", "#fff"), position);

        lobby.players = Arrays.copyOf(lobby.players, position + 1);
        lobby.players[position] = lobbyPlayer;
        lobbyInterfaceRepository.save(lobby);

        session.sendMessage(new TextMessage("token:" + token));
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
        for (WebSocketSession session : sessions.get("hey")) {
            session.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}