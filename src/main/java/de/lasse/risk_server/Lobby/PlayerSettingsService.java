package de.lasse.risk_server.Lobby;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import de.lasse.risk_server.Database.Lobby.Color;
import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;
import de.lasse.risk_server.Database.Maps.MapInterfaceRepository;
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
    MapInterfaceRepository mapInterfaceRepository;

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

    public MessageBroadcastTuple changeFlagPosition(double flagx, double flagy,
            QueryIdentification queryIdentification) {
        Optional<Lobby> lobby_opt = lobbyInterfaceRepository.findById(queryIdentification.lobbyId);
        if (!lobby_opt.isPresent())
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("lobby not valid"),
                    queryIdentification.session);

        Lobby lobby = lobby_opt.get();

        int playerIndex = getPlayerIndex(queryIdentification.token, lobby);
        if (playerIndex == -1)
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("token not valid"),
                    queryIdentification.session);

        if (!this.flagPosition.isInside(lobby.mapId, flagx, flagy))
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("coordinates not valid"),
                    queryIdentification.session);

        LobbyPlayer player = lobby.players[playerIndex];

        player.flagx = flagx;
        player.flagy = flagy;
        lobbyInterfaceRepository.save(lobby);

        JSONObject out = new JSONObject();
        out.put("playerid", player.id);
        out.put("flagx", flagx);
        out.put("flagy", flagy);

        return new MessageBroadcastTuple(WebSocketHelper.generateTextMessage("flagposition_update", out),
                queryIdentification.lobbyId);
    }

    private MessageBroadcastTuple changeColor(String newColor, QueryIdentification queryIdentification) {
        Optional<Lobby> lobby_opt = lobbyInterfaceRepository.findById(queryIdentification.lobbyId);
        if (!lobby_opt.isPresent())
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("lobby not valid"),
                    queryIdentification.session);

        Lobby lobby = lobby_opt.get();

        int playerIndex = getPlayerIndex(queryIdentification.token, lobby);
        if (playerIndex == -1)
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("token not valid"),
                    queryIdentification.session);

        if (colorIsOccupied(newColor, lobby))
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("color already used"),
                    queryIdentification.session);

        Optional<Color> color = getColor(newColor);
        if (!color.isPresent())
            return new MessageBroadcastTuple(WebSocketHelper.generateDeclineMessage("color not valid"),
                    queryIdentification.session);

        lobby.players[playerIndex].color = color.get();
        lobbyInterfaceRepository.save(lobby);

        LobbyPlayer player = lobby.players[playerIndex];

        JSONObject out = new JSONObject();
        out.put("playerid", player.id);
        out.put("color", color.get().toJsonObject());

        return new MessageBroadcastTuple(WebSocketHelper.generateTextMessage("color_change", out),
                queryIdentification.lobbyId);
    }

    public void performColorChange(String newColor, QueryIdentification queryIdentification) {
        try {
            this.changeColor(newColor, queryIdentification).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void performFlagPositionChange(double flagx, double flagy, QueryIdentification queryIdentification) {
        try {
            this.changeFlagPosition(flagx, flagy, queryIdentification).execute();
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
        return colors.stream().filter(c -> c.hex.equalsIgnoreCase(hex)).findFirst();
    }

}
