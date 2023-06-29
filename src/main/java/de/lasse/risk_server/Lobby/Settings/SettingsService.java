package de.lasse.risk_server.Lobby.Settings;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Game.GameHandler;
import de.lasse.risk_server.Game.Game.Game;
import de.lasse.risk_server.Game.Game.GameGenerator;
import de.lasse.risk_server.Game.Game.GameInterfaceRepository;
import de.lasse.risk_server.Lobby.LobbyHandler;
import de.lasse.risk_server.Lobby.Lobby.Lobby;
import de.lasse.risk_server.Lobby.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.WebSocketHelper;

@Service
public class SettingsService {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    GameInterfaceRepository gameInterfaceRepository;

    @Autowired
    PlayerSettingsService playerSettingsService;

    @Autowired
    GameGenerator gameGenerator;

    public boolean isAuthorized(Lobby lobby, String token) {
        Optional<LobbyPlayer> player = Arrays.stream(lobby.getPlayers())
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

        Game game = gameGenerator.generateGame(lobby);
        gameInterfaceRepository.save(game);

        lobbyInterfaceRepository.delete(lobby);

        // Initialize GameHandler
        // game.getId() == lobby.getId() but code like this robuster.
        GameHandler.sessions.put(game.getId(), LobbyHandler.sessions.remove(lobby.getId()));

        // Inform Players
        LobbyHandler.broadcast(WebSocketHelper.generateTextMessage("start_game"), lobby.getId());
        System.out.println("Successfully created new Game!");
    }

}
