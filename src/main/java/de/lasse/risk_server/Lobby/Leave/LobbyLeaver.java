package de.lasse.risk_server.Lobby.Leave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import de.lasse.risk_server.Lobby.LobbyHandler;
import de.lasse.risk_server.Lobby.Lobby.Lobby;
import de.lasse.risk_server.Lobby.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;
import de.lasse.risk_server.Lobby.Settings.PlayerSettingsService;
import de.lasse.risk_server.Shared.WebSocketHelper;

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
                LobbyPlayer player = lobby.getLobbyPlayers()[i];
                String hostid = removeSession(lobby, player);

                boolean lobbyAlive = hostid != null;
                if (!lobbyAlive)
                    return;

                Map<String, Object> out = new HashMap<>();
                out.put("id", player.getId());
                out.put("host", hostid);

                LobbyHandler.broadcast(WebSocketHelper.generateTextMessage("player_quit", out),
                        lobby.getId());

            }
        }
    }

    public String removeSession(Lobby lobby, LobbyPlayer removingPlayer) {

        if (lobby.getLobbyPlayers().length <= 1) {
            lobbyInterfaceRepository.delete(lobby);
            LobbyHandler.sessions.remove(lobby.getId());
            return null;
        }

        LobbyPlayer[] newLobbyPlayers = new LobbyPlayer[lobby.getLobbyPlayers().length - 1];
        String host = null;

        int appendIndex = 0;
        for (int x = 0; x < lobby.getLobbyPlayers().length; x++) {
            if (lobby.getLobbyPlayers()[x].getId().equals(removingPlayer.getId()))
                continue;

            LobbyPlayer next = lobby.getLobbyPlayers()[x];
            if (appendIndex == 0) {
                next.setHost(true);
                host = next.getId();
            }

            newLobbyPlayers[appendIndex] = next;
            appendIndex++;
        }

        lobby.setLobbyPlayers(newLobbyPlayers);
        lobbyInterfaceRepository.save(lobby);
        return host;
    }

}
