package de.lasse.risk_server.Database.Lobby;

import org.json.JSONObject;

import nonapi.io.github.classgraph.json.Id;

public class DisplayLobby {

    @Id
    private String id;

    private int playerCount;

    private int maxPlayers;

    private String mapId;

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("mapId", mapId);
        out.put("playerCount", playerCount);
        out.put("maxPlayers", maxPlayers);
        return out;
    }
}
