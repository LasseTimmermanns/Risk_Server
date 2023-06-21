package de.lasse.risk_server.Lobby;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Database.Lobby.Color;
import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;
import de.lasse.risk_server.Database.Maps.DisplayMap;
import de.lasse.risk_server.Database.Maps.DisplayMapInterfaceRepository;
import de.lasse.risk_server.Database.Settings.ColorInterfaceRepository;

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
    public static String colorsString;

    @PostConstruct
    public void init() {
        colors = colorInterfaceRepository.findAll();
        JSONArray json = new JSONArray();
        for (Color c : colors)
            json.put(c.toJsonObject());
        colorsString = json.toString();
    }

    public Color getUnoccupiedColor(Lobby lobby) {
        int offset = (int) (Math.random() * colors.size());
        for (int i = 0; i < colors.size(); i++) {
            Color c = colors.get((offset + i) % colors.size());
            if (!colorIsOccupied(c.hex, lobby))
                return c;
        }
        return null;
    }

    public void changeFlagPosition(double flagx, double flagy,
            QueryIdentification queryIdentification) throws IOException {

        Object[] identification = identificate(queryIdentification);
        Lobby lobby = (Lobby) identification[0];
        LobbyPlayer player = (LobbyPlayer) identification[1];

        if (!this.flagPosition.isInside(lobby.mapId, flagx, flagy)) {
            queryIdentification.session.sendMessage(WebSocketHelper.generateDeclineMessage("coordinates not valid"));
            return;
        }

        changeFlagPosition(flagx, flagy, lobby, player);
    }

    public void changeFlagPosition(double flagx, double flagy, Lobby lobby, LobbyPlayer player) throws IOException {
        player.flagx = flagx;
        player.flagy = flagy;
        lobbyInterfaceRepository.save(lobby);

        JSONObject out = new JSONObject();
        out.put("playerid", player.id);
        out.put("flagx", flagx);
        out.put("flagy", flagy);

        LobbyHandler.broadcast(WebSocketHelper.generateTextMessage("flagposition_update", out), lobby.id);
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

        player.color = color.get();
        lobbyInterfaceRepository.save(lobby);

        JSONObject out = new JSONObject();
        out.put("playerid", player.id);
        out.put("color", color.get().toJsonObject());

        LobbyHandler.broadcast(WebSocketHelper.generateTextMessage("color_change", out), queryIdentification.lobbyId);
    }

    public void updateAllFlagPositions(Lobby lobby) throws IOException {
        DisplayMap map = this.displayMapInterfaceRepository.findDisplayMapById(lobby.mapId);
        for (LobbyPlayer player : lobby.players) {
            double[] pos = flagPosition.generateRandomValidCoordinates(map);
            this.changeFlagPosition(pos[0], pos[1], lobby, player);
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
        return colors.stream().filter(c -> c.hex.equalsIgnoreCase(hex)).findFirst();
    }

    public Object[] identificate(QueryIdentification queryIdentification) throws IOException {
        Optional<Lobby> lobby_opt = lobbyInterfaceRepository.findById(queryIdentification.lobbyId);

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

        return new Object[] { lobby, lobby.players[playerIndex] };
    }
}
