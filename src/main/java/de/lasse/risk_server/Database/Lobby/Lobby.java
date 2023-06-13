package de.lasse.risk_server.Database.Lobby;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

import nonapi.io.github.classgraph.json.Id;

@Document(collection = "lobbies")
public class Lobby {

    @Id
    public String id;

    public int maxPlayers;

    public int turnTimer;

    public String cardBonus;

    public String mapName;

    public long creationDate;

    public LobbyPlayer[] players;

    public boolean isPublic;

    public Lobby(int maxPlayers, int turnTimer, String cardBonus, String mapName, long creationDate,
            LobbyPlayer[] players, boolean isPublic) {
        this.maxPlayers = maxPlayers;
        this.turnTimer = turnTimer;
        this.cardBonus = cardBonus;
        this.mapName = mapName;
        this.players = players;
        this.creationDate = creationDate;
        this.isPublic = isPublic;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("mapName", mapName);
        out.put("maxPlayers", maxPlayers);
        out.put("turnTimer", turnTimer);
        out.put("isPublic", isPublic);
        out.put("cardBonus", cardBonus);

        JSONArray playerArray = new JSONArray();
        for (LobbyPlayer player : players) {
            playerArray.put(player.toJsonObject());
        }

        out.put("players", playerArray);
        return out;
    }

    public static Lobby generateDefault() {
        Lobby out = new Lobby(6, 60, "fixed", "classic", System.currentTimeMillis(), new LobbyPlayer[0], false);
        return out;
    }
}
