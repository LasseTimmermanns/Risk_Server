package de.lasse.risk_server.Lobby;

import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;

@Service
public class LobbyLeaver {

    @Autowired
    SettingsService settingsService;

    @Autowired
    LobbyHandler lobbyHandler;

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    public void leave(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        for (Entry<String, List<WebSocketSession>> lobbyEntry : LobbyHandler.sessions.entrySet()) {
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

                lobbyHandler.broadcast(
                        WebSocketHelper.generateTextMessage("player_quit",
                                new JSONObject("{'playername':'" + player.name + "'}")),
                        lobby.id);
            }
        }
    }

}
