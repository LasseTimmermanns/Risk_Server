package de.lasse.risk_server.Lobby;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;
import de.lasse.risk_server.Utils.QueryUtil;
import de.lasse.risk_server.Utils.TokenGenerator;

@Service
public class LobbyHandler extends TextWebSocketHandler {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    PlayerSettingsService playerSettingsService;

    @Autowired
    SettingsService settingsService;

    @Autowired
    LobbyLeaver lobbyLeaver;

    public static HashMap<String, List<WebSocketSession>> sessions = new HashMap<String, List<WebSocketSession>>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String lobbyid = QueryUtil.getQueryValue("lobby", session.getUri().getQuery());
        String playername = QueryUtil.getQueryValue("playername", session.getUri().getQuery());

        if (lobbyid == null || playername == null) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Not enough data provided"));
            session.close();
            return;
        }

        Lobby lobby = lobbyInterfaceRepository.findById(lobbyid).orElse(null);
        if (lobby == null || !sessions.containsKey(lobbyid)) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Lobby not found"));
            session.close();
            return;
        }

        if (lobby.players.length >= lobby.maxPlayers) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Lobby is full"));
            session.close();
            return;
        }

        int position = lobby.players.length;
        String token = TokenGenerator.generateToken();
        String uuid = TokenGenerator.generateToken();

        LobbyPlayer lobbyPlayer = new LobbyPlayer(uuid, playername, token, position == 0,
                playerSettingsService.getUnoccupiedColor(lobby), position);

        lobby.players = Arrays.copyOf(lobby.players, position + 1);
        lobby.players[position] = lobbyPlayer;
        lobbyInterfaceRepository.save(lobby);

        session.sendMessage(
                WebSocketHelper.generateTextMessage("token_granted", new JSONObject("{'token':'" + token + "'}")));

        session.sendMessage(WebSocketHelper.generateTextMessage("join_accepted", lobby.toJsonObject()));

        broadcast(WebSocketHelper.generateTextMessage("join", lobbyPlayer.toJsonObject()), lobbyid);
        sessions.get(lobbyid).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        JSONObject message_json = new JSONObject(message.getPayload());

        JSONObject data = message_json.has("data") ? message_json.getJSONObject("data") : null;

        switch (message_json.getString("event")) {
            case "color_change":
                playerSettingsService.performColorChange(data.getString("lobbyid"), data.getString("token"),
                        data.getString("hex"), session);
                break;
            case "leave":
                session.close();
                break;
            case "privacy_change":
                settingsService.changeVisibility(data.getString("lobbyid"), data.getBoolean("isPublic"),
                        data.getString("token"), session);
                break;
            default:
                System.out.println("Message not handled in LobbyHandler");
                System.out.println(message_json.toString());
                break;
        }
    }

    public static void broadcast(TextMessage message, String lobbyid) throws IOException {
        for (WebSocketSession session : sessions.get(lobbyid)) {
            if (!session.isOpen()) {
                System.out.println("Try Broadcast but session is closed");
                continue;
            }

            session.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Session Closed");
        lobbyLeaver.leave(session);
    }

}