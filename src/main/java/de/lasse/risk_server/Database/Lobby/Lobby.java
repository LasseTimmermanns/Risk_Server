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

    public String mapId;

    public long creationDate;

    public LobbyPlayer[] players;

    public Lobby(int maxPlayers, int turnTimer, String cardBonus, String mapId, long creationDate,
            LobbyPlayer[] players) {
        this.maxPlayers = maxPlayers;
        this.turnTimer = turnTimer;
        this.cardBonus = cardBonus;
        this.mapId = mapId;
        this.players = players;
        this.creationDate = creationDate;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("mapId", mapId);

        JSONArray playerArray = new JSONArray();
        for (LobbyPlayer player : players) {
            playerArray.put(player.toJsonObject());
        }

        out.put("players", playerArray);
        return out;
    }

    public static Lobby generateDefault() {
        Lobby out = new Lobby(6, 60, "fixed", "classic", System.currentTimeMillis(), new LobbyPlayer[0]);
        return out;
    }
}
