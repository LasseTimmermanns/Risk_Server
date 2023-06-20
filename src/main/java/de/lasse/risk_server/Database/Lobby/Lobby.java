package de.lasse.risk_server.Database.Lobby;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import nonapi.io.github.classgraph.json.Id;

@Document(collection = "lobbies")
public class Lobby {

    @Id
    public String id;

    @Field("max_players")
    public int maxPlayers;

    @Field("turn_timer")
    public int turnTimer;

    @Field("is_fixed")
    public boolean isFixed;

    @Field("map_id")
    public String mapId;

    @Field("creation_date")
    public long creationDate;

    public LobbyPlayer[] players;

    @Field("is_public")
    public boolean isPublic;

    public Lobby(int maxPlayers, int turnTimer, boolean isFixed, String mapId, long creationDate,
            LobbyPlayer[] players, boolean isPublic) {
        this.maxPlayers = maxPlayers;
        this.turnTimer = turnTimer;
        this.isFixed = isFixed;
        this.mapId = mapId;
        this.players = players;
        this.creationDate = creationDate;
        this.isPublic = isPublic;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("map_id", mapId);
        out.put("max_players", maxPlayers);
        out.put("turn_timer", turnTimer);
        out.put("is_public", isPublic);
        out.put("is_fixed", this.isFixed);

        JSONArray playerArray = new JSONArray();
        for (LobbyPlayer player : players) {
            playerArray.put(player.toJsonObject());
        }

        out.put("players", playerArray);
        return out;
    }

    public static Lobby generateDefault() {
        Lobby out = new Lobby(6, 60, true, "classic", System.currentTimeMillis(), new LobbyPlayer[0], false);
        return out;
    }
}
