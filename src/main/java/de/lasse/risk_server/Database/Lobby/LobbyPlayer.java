package de.lasse.risk_server.Database.Lobby;

import org.json.JSONObject;

import nonapi.io.github.classgraph.json.Id;

public class LobbyPlayer {

    @Id
    private String id;

    private String name, color;

    private int position;

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("name", name);
        out.put("color", color);
        out.put("position", position);
        return out;
    }
}
