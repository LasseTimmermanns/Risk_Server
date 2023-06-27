package de.lasse.risk_server.Lobby.Settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Lobby.LobbyHandler;
import de.lasse.risk_server.Lobby.Display.DisplayMap.DisplayMap;
import de.lasse.risk_server.Lobby.Display.DisplayMap.DisplayMapInterfaceRepository;
import de.lasse.risk_server.Lobby.Flag.FlagPosition;
import de.lasse.risk_server.Lobby.Lobby.Lobby;
import de.lasse.risk_server.Lobby.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.WebSocketHelper;
import de.lasse.risk_server.Shared.Color.Color;
import de.lasse.risk_server.Shared.Color.ColorInterfaceRepository;

@Service
public class PlayerSettingsService {

    @Autowired
    FlagPosition flagPosition;
    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    ColorInterfaceRepository colorInterfaceRepository;

    @Autowired
    DisplayMapInterfaceRepository displayMapInterfaceRepository;

    public static List<Color> colors = null;

    @PostConstruct
    public void init() {
        colors = colorInterfaceRepository.findAll();
    }

    public Color getUnoccupiedColor(Lobby lobby) {
        int offset = (int) (Math.random() * colors.size());
        for (int i = 0; i < colors.size(); i++) {
            Color c = colors.get((offset + i) % colors.size());
            if (!colorIsOccupied(c.getHex(), lobby))
                return c;
        }
        return null;
    }

    public void changeFlagPosition(double flagx, double flagy,
            QueryIdentification queryIdentification) throws IOException {

        Object[] identification = identificate(queryIdentification);
        Lobby lobby = (Lobby) identification[0];
        LobbyPlayer player = (LobbyPlayer) identification[1];

        if (!this.flagPosition.isInside(lobby.getMapId(), flagx, flagy)) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("coordinates not valid"));
            return;
        }

        changeFlagPosition(flagx, flagy, lobby, player);
    }

    public void changeFlagPosition(double flagx, double flagy, Lobby lobby, LobbyPlayer player) throws IOException {
        player.setFlagx(flagx);
        player.setFlagy(flagy);
        lobbyInterfaceRepository.save(lobby);

        Map<String, Object> out = new HashMap<>();
        out.put("playerid", player.getId());
        out.put("flagx", flagx);
        out.put("flagy", flagy);

        LobbyHandler.broadcast(WebSocketHelper.generateTextMessage("flagposition_update", out), lobby.getId());
    }

    public void changeColor(String newColor, QueryIdentification queryIdentification) throws IOException {
        Object[] identification = identificate(queryIdentification);
        Lobby lobby = (Lobby) identification[0];
        LobbyPlayer player = (LobbyPlayer) identification[1];

        if (colorIsOccupied(newColor, lobby)) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("color already used"));
            return;
        }

        Optional<Color> color = getColor(newColor);
        if (!color.isPresent()) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("color already used"));
            return;
        }

        player.setColor(color.get());
        lobbyInterfaceRepository.save(lobby);

        Map<String, Object> out = new HashMap<>();
        out.put("playerid", player.getId());
        out.put("color", color.get());

        LobbyHandler.broadcast(WebSocketHelper.generateTextMessage("color_change", out), queryIdentification.roomId);
    }

    public void updateAllFlagPositions(Lobby lobby) throws IOException {
        DisplayMap map = this.displayMapInterfaceRepository.findDisplayMapById(lobby.getMapId());
        for (LobbyPlayer player : lobby.getLobbyPlayers()) {
            double[] pos = flagPosition.generateRandomValidCoordinates(map);
            this.changeFlagPosition(pos[0], pos[1], lobby, player);
        }
    }

    public int getPlayerIndex(String token, Lobby lobby) {
        for (int i = 0; i < lobby.getLobbyPlayers().length; i++) {
            LobbyPlayer current = lobby.getLobbyPlayers()[i];
            if (current.getToken().equals(token))
                return i;
        }
        return -1;
    }

    public boolean colorIsOccupied(String hex, Lobby lobby) {
        for (LobbyPlayer p : lobby.getLobbyPlayers()) {
            if (p.getColor().getHex().equalsIgnoreCase(hex))
                return true;
        }
        return false;
    }

    public Optional<Color> getColor(String hex) {
        return colors.stream().filter(c -> c.getHex().equalsIgnoreCase(hex)).findFirst();
    }

    public Object[] identificate(QueryIdentification queryIdentification) throws IOException {
        Optional<Lobby> lobby_opt = lobbyInterfaceRepository.findById(queryIdentification.roomId);

        if (!lobby_opt.isPresent()) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("lobby not valid"));
            return null;
        }

        Lobby lobby = lobby_opt.get();

        int playerIndex = getPlayerIndex(queryIdentification.token, lobby);
        if (playerIndex == -1) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("token not valid"));
            return null;
        }

        return new Object[] { lobby, lobby.getLobbyPlayers()[playerIndex] };
    }

    public static List<Color> getColors() {
        return colors;
    }
}
