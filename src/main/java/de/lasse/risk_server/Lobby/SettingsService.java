package de.lasse.risk_server.Lobby;

import java.util.Arrays;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;

@Service
public class SettingsService {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    public boolean isAuthorized(Lobby lobby, String token) {
        Optional<LobbyPlayer> player = Arrays.stream(lobby.players).filter(p -> p.host && p.token.equals(token))
                .findFirst();
        return player.isPresent();
    }

    public Lobby authorizationCheck(QueryIdentification queryIdentification) throws Exception {
        Lobby lobby = lobbyInterfaceRepository.findById(queryIdentification.lobbyId).orElseThrow();
        if (!isAuthorized(lobby, queryIdentification.token)) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("Not Authorized"));
            System.out.println("Player not Authorized to change Visibility");
            return null;
        }
        return lobby;
    }

    public void changeVisibility(boolean isPublic, QueryIdentification queryIdentification)
            throws Exception {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.isPublic = isPublic;
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event,
                        new JSONObject("{'is_public':" + isPublic + "}")),
                lobby.id);
    }

    public void changeCardBonus(boolean isFixed, QueryIdentification queryIdentification)
            throws Exception {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.isFixed = isFixed;
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event,
                        new JSONObject("{'is_fixed':" + isFixed + "}")),
                lobby.id);
    }

    public void changeTurnTimer(int turnTimer, QueryIdentification queryIdentification)
            throws Exception {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.turnTimer = turnTimer;
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event,
                        new JSONObject("{'turn_timer':" + turnTimer + "}")),
                lobby.id);
    }

    public void changeMaxPlayers(int maxPlayers, QueryIdentification queryIdentification) throws Exception {
        Lobby lobby = authorizationCheck(queryIdentification);
        if (lobby == null)
            return;

        lobby.maxPlayers = maxPlayers;
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage(queryIdentification.event,
                        new JSONObject("{'max_players':" + maxPlayers + "}")),
                lobby.id);
    }
}
