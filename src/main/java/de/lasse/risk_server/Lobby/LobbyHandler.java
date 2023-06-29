package de.lasse.risk_server.Lobby;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.lasse.risk_server.Game.Map.MapInterfaceRepository;
import de.lasse.risk_server.Lobby.Flag.FlagPosition;
import de.lasse.risk_server.Lobby.Leave.LobbyLeaver;
import de.lasse.risk_server.Lobby.Lobby.Lobby;
import de.lasse.risk_server.Lobby.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;
import de.lasse.risk_server.Lobby.Settings.PlayerSettingsService;
import de.lasse.risk_server.Lobby.Settings.SettingsService;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.QueryUtil;
import de.lasse.risk_server.Shared.TokenGenerator;
import de.lasse.risk_server.Shared.WebSocketHelper;

@Service
public class LobbyHandler extends TextWebSocketHandler {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    PlayerSettingsService playerSettingsService;

    @Autowired
    SettingsService settingsService;

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    @Autowired
    LobbyLeaver lobbyLeaver;

    @Autowired
    FlagPosition flagPosition;

    public static HashMap<String, List<WebSocketSession>> sessions = new HashMap<String, List<WebSocketSession>>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String lobbyId = QueryUtil.getQueryValue("lobby", session.getUri().getQuery());
        String playername = QueryUtil.getQueryValue("playername", session.getUri().getQuery());

        if (lobbyId == null || playername == null) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Not enough data provided"));
            session.close();
            return;
        }

        Lobby lobby = lobbyInterfaceRepository.findById(lobbyId).orElse(null);
        if (lobby == null || !sessions.containsKey(lobbyId)) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Lobby not found"));
            session.close();
            return;
        }

        if (lobby.getPlayers().length >= lobby.getMaxPlayers()) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Lobby is full"));
            session.close();
            return;
        }

        int position = lobby.getPlayers().length;
        String token = TokenGenerator.generateToken();
        String uuid = TokenGenerator.generateToken();

        double[] flag_position = flagPosition.generateRandomValidCoordinates(lobby.getMapId());

        LobbyPlayer lobbyPlayer = new LobbyPlayer(uuid, playername, token, position == 0,
                playerSettingsService.getUnoccupiedColor(lobby), position, flag_position[0], flag_position[1]);

        LobbyPlayer[] players = Arrays.copyOf(lobby.getPlayers(), position + 1);
        players[position] = lobbyPlayer;
        lobby.setPlayers(players);

        lobbyInterfaceRepository.save(lobby);

        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("playerId", uuid);

        session.sendMessage(WebSocketHelper.generateTextMessage("token_granted", map));

        session.sendMessage(WebSocketHelper.generateTextMessage("join_accepted", lobby));

        broadcast(WebSocketHelper.generateTextMessage("join", lobbyPlayer), lobbyId);

        sessions.get(lobbyId).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        JsonNode message_json = new ObjectMapper().readTree(message.getPayload());

        JsonNode data = message_json.has("data") ? message_json.get("data") : null;
        QueryIdentification queryIdentification = null;

        try {
            queryIdentification = new QueryIdentification(message_json.get("event").asText(),
                    data.get("lobbyId").asText(), data.get("token").asText(), session);
        } catch (NullPointerException e) {
        }

        try {
            switch (message_json.get("event").asText()) {
                case "color_change":
                    playerSettingsService.changeColor(data.get("hex").asText(), queryIdentification);
                    break;
                case "leave":
                    session.close();
                    break;
                case "privacy_change":
                    settingsService.changeVisibility(data.get("value").asBoolean(), queryIdentification);
                    break;
                case "max_players_change":
                    settingsService.changeMaxPlayers(data.get("value").asInt(), queryIdentification);
                    break;
                case "turn_timer_change":
                    settingsService.changeTurnTimer(data.get("value").asInt(), queryIdentification);
                    break;
                case "card_bonus_change":
                    settingsService.changeCardBonus(data.get("value").asBoolean(), queryIdentification);
                    break;
                case "map_change":
                    settingsService.changeMap(data.get("value").asText(), queryIdentification);
                    break;
                case "flagposition_update":
                    playerSettingsService.changeFlagPosition(data.get("flagx").asDouble(), data.get("flagy").asDouble(),
                            queryIdentification);
                    break;
                case "start_game":
                    settingsService.startGame(queryIdentification);
                    break;
                default:
                    System.out.println("Message not handled in LobbyHandler");
                    System.out.println(message_json.toString());
                    break;
            }
        } catch (NullPointerException e) {
            System.out.println(message_json.toString());
            e.printStackTrace();
            System.out.println("Nullpointer in LobbyHandler");
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Bad Request"));
        }
    }

    public static void broadcast(TextMessage message, String lobbyId) throws IOException {
        for (WebSocketSession session : sessions.get(lobbyId)) {
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