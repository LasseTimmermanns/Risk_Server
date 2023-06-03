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
    PlayerSettingsService settingsService;

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    public void leave(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        for (Entry<String, List<WebSocketSession>> lobbyEntry : LobbyHandler.sessions.entrySet()) {
            String lobbyId = lobbyEntry.getKey();
            List<WebSocketSession> lobbySessions = lobbyEntry.getValue();

            for (int i = 0; i < lobbyEntry.getValue().size(); i++) {
                WebSocketSession playerSession = lobbySessions.get(i);
                if (!sessionId.equals(playerSession.getId()))
                    continue;

                lobbyEntry.getValue().remove(i);
                Lobby lobby = lobbyInterfaceRepository.findById(lobbyId).orElseThrow();
                LobbyPlayer player = lobby.players[i];
                removeSession(lobby, player);

                LobbyHandler.broadcast(
                        WebSocketHelper.generateTextMessage("player_quit",
                                new JSONObject("{'id':'" + player.id + "'}")),
                        lobby.id);
            }
        }
    }

    public void removeSession(Lobby lobby, LobbyPlayer removingPlayer) {
        if (lobby.players.length <= 1) {
            lobbyInterfaceRepository.delete(lobby);
            LobbyHandler.sessions.remove(lobby.id);
            return;
        }

        LobbyPlayer[] newLobbyPlayers = new LobbyPlayer[lobby.players.length - 1];

        int appendIndex = 0;
        for (int x = 0; x < lobby.players.length; x++) {
            if (lobby.players[x].id.equals(removingPlayer.id))
                continue;

            newLobbyPlayers[appendIndex] = lobby.players[x];
            appendIndex++;
        }

        lobby.players = newLobbyPlayers;
        lobbyInterfaceRepository.save(lobby);
    }

}
