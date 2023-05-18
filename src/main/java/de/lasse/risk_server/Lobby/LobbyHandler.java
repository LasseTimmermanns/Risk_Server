package de.lasse.risk_server.Lobby;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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

    @Autowired
    SettingsService settingsService;

    public static HashMap<String, List<WebSocketSession>> sessions = new HashMap<>();

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
        LobbyPlayer lobbyPlayer = new LobbyPlayer(uuid, playername, token, new Color("white", "#fff"), position);

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
        JSONObject data = message_json.getJSONObject("data");
        switch (message_json.getString("event")) {
            case "color_change":
                settingsService.performColorChange(data.getString("lobbyid"), data.getString("token"),
                        data.getString("hex"), session);
                break;
            default:
                System.out.println("Message not handled in LobbyHandler");
                System.out.println(message_json.toString());
                break;
        }
    }

    private void broadcast(TextMessage message, String lobbyid) throws IOException {
        for (WebSocketSession session : sessions.get(lobbyid)) {
            session.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        for (Entry<String, List<WebSocketSession>> lobbyEntry : sessions.entrySet()) {
            for (int i = 0; i < lobbyEntry.getValue().size(); i++) {
                WebSocketSession playerSession = lobbyEntry.getValue().get(i);
                if (!sessionId.equals(playerSession.getId()))
                    continue;
                Lobby lobby = lobbyInterfaceRepository.findById(lobbyEntry.getKey()).orElseThrow();

                if (lobby.players.length <= 1) {
                    lobbyInterfaceRepository.delete(lobby);
                    return;
                }

                LobbyPlayer[] newLobbyPlayers = new LobbyPlayer[lobby.players.length - 1];
                LobbyPlayer player = lobby.players[i];

                int appendIndex = 0;
                for (int x = 0; x < lobby.players.length; x++) {
                    if (x == i)
                        continue;

                    newLobbyPlayers[appendIndex] = lobby.players[i];
                    appendIndex++;
                }
                lobby.players = newLobbyPlayers;

                // TODO: if multiple are at the exactly same time, there will be bug
                lobbyEntry.getValue().remove(i);
                lobbyInterfaceRepository.save(lobby);

                broadcast(
                        WebSocketHelper.generateTextMessage("player_quit",
                                new JSONObject("{'playername':'" + player.name + "'}")),
                        lobby.id);
            }
        }
    }

}