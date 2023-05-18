package de.lasse.risk_server.Lobby;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.websocket.Decoder.Text;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import de.lasse.risk_server.Database.Lobby.Color;
import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;
import de.lasse.risk_server.Database.Settings.ColorInterfaceRepository;

@Service
public class SettingsService {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    ColorInterfaceRepository colorInterfaceRepository;

    private MessageBroadcastTuple changeColor(String providedLobbyId, String providedToken,
            String newColor, WebSocketSession session) {
        Optional<Lobby> lobby_opt = lobbyInterfaceRepository.findById(providedLobbyId);
        if (!lobby_opt.isPresent())
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("lobby not valid"),
                    session);

        Lobby lobby = lobby_opt.get();

        int playerIndex = getPlayerIndex(providedToken, lobby);
        if (playerIndex == -1)
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("token not valid"), session);

        if (colorIsOccupied(newColor, lobby))
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("color already used"), session);

        Optional<Color> color = getColor(newColor);
        if (!color.isPresent())
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("color not valid"), session);

        lobby.players[playerIndex].color = color.get();
        lobbyInterfaceRepository.save(lobby);

        LobbyPlayer player = lobby.players[playerIndex];

        JSONObject out = new JSONObject();
        out.put("playerid", player.id);
        out.put("color", color.get().hex);

        return new MessageBroadcastTuple(WebSocketHelper.generateTextMessage("color_change", out), providedLobbyId);
    }

    public void performColorChange(String providedLobbyId, String providedToken,
            String newColor, WebSocketSession session) {
        try {
            this.changeColor(providedLobbyId, providedToken, newColor, session).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerIndex(String token, Lobby lobby) {
        for (int i = 0; i < lobby.players.length; i++) {
            LobbyPlayer current = lobby.players[i];
            if (current.token.equals(token))
                return i;
        }
        return -1;
    }

    public boolean colorIsOccupied(String hex, Lobby lobby) {
        for (LobbyPlayer p : lobby.players) {
            if (p.color.hex.equalsIgnoreCase(hex))
                return true;
        }
        return false;
    }

    public Optional<Color> getColor(String hex) {
        Optional<Color> foundColor = colorInterfaceRepository.findByHex(hex);
        return foundColor;
    }
}
