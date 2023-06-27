package de.lasse.risk_server.Lobby.Settings;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Game.Map.MapTerritory;
import de.lasse.risk_server.Game.Players.Player;
import de.lasse.risk_server.Game.Settings.SettingsState;
import de.lasse.risk_server.Lobby.LobbyHandler;
import de.lasse.risk_server.Lobby.Lobby.Lobby;
import de.lasse.risk_server.Lobby.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.WebSocketHelper;
import de.lasse.risk_server.Shared.Color.Color;

@Service
public class SettingsService {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    PlayerSettingsService playerSettingsService;

    public boolean isAuthorized(Lobby lobby, String token) {
        Optional<LobbyPlayer> player = Arrays.stream(lobby.getLobbyPlayers())
                .filter(p -> p.getHost() && p.getToken().equals(token))
                .findFirst();
        return player.isPresent();
    }

    public Lobby authorizationCheck(QueryIdentification queryIdentification) throws IOException {
        Lobby lobby = lobbyInterfaceRepository.findById(queryIdentification.roomId).orElseThrow();
        if (!isAuthorized(lobby, queryIdentification.token)) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("Not Authorized"));
            System.out.println("Player not Authorized to change Visibility");
            return null;
        }
        return lobby;
    }

    public void changeVisibility(boolean isPublic, QueryIdentification queryIdentification)
            throws IOException {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.setPublic(isPublic);
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(WebSocketHelper.generateTextMessage(queryIdentification.event,
                Map.of("value", isPublic)), lobby.getId());
    }

    public void changeCardBonus(boolean isFixed, QueryIdentification queryIdentification)
            throws IOException {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.setFixed(isFixed);
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event, Map.of("value", isFixed)),
                lobby.getId());
    }

    public void changeTurnTimer(int turnTimer, QueryIdentification queryIdentification)
            throws IOException {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.setTurnTimer(turnTimer);
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event, Map.of("value", turnTimer)),
                lobby.getId());
    }

    public void changeMaxPlayers(int maxPlayers, QueryIdentification queryIdentification) throws IOException {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.setMaxPlayers(maxPlayers);
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event, Map.of("value", maxPlayers)),
                lobby.getId());
    }

    public void changeMap(String mapId, QueryIdentification queryIdentification) throws IOException {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.setMapId(mapId);
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event, Map.of("value", mapId)),
                lobby.getId());

        playerSettingsService.updateAllFlagPositions(lobby);
    }

    public void startGame(QueryIdentification queryIdentification) throws IOException {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        // Create New Game

        // id;
        // mapId;
        // players;
        // territories;
        // move;
        // settings;

        String gameId = lobby.getId();
        String mapId = lobby.getMapId();
        int move = 0;
        SettingsState settingsState = new SettingsState(lobby.isFixed());
        Player[] players = getPlayers(lobby.getLobbyPlayers());

        // Territories

        // Initialize GameHandler

        // Inform Players
    }

    private List<Integer> generateRandomOrder(int range) {
        List<Integer> order = IntStream.range(0, range).boxed().collect(Collectors.toList());
        Collections.shuffle(order);
        return order;
    }

    private Player[] getPlayers(LobbyPlayer[] lobbyPlayers) {
        Player[] out = new Player[lobbyPlayers.length];
        List<Integer> order = generateRandomOrder(lobbyPlayers.length);
        for (int i = 0; i < lobbyPlayers.length; i++) {
            LobbyPlayer p = lobbyPlayers[order.get(i)];

            String id = p.getId();
            String token = p.getToken();
            String name = p.getName();
            Color color = p.getColor();
            int seat = order.get(i);

            out[i] = new Player(id, token, name, color, seat);
        }

        return out;
    }

    public MapTerritory[] generateTerritories(Player[] players, String mapId) {
        // MapTerritory[] map = mapRepository.findTerritories(mapId);
        return null;
    }
}
