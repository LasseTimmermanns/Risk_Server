package de.lasse.risk_server.Lobby;

import java.util.Arrays;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

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

    public void changeVisibility(String lobbyid, boolean isPublic, String token, WebSocketSession session)
            throws Exception {
        Lobby lobby = lobbyInterfaceRepository.findById(lobbyid).orElseThrow();
        if (!isAuthorized(lobby, token)) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("Not Authorized"));
            System.out.println("Player not Authorized to change Visibility");
            return;
        }

        System.out.println("RequestedPublic " + Boolean.toString(isPublic));
        lobby.isPublic = isPublic;
        lobbyInterfaceRepository.save(lobby);

        LobbyHandler.broadcast(
                WebSocketHelper.generateTextMessage("privacy_change",
                        new JSONObject("{'isPublic':" + isPublic + "}")),
                lobby.id);
    }

}
