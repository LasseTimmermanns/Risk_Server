package de.lasse.risk_server.Database.Lobby;

import org.json.JSONObject;

import nonapi.io.github.classgraph.json.Id;

public class LobbyPlayer {

    @Id
    private String id;

    private String name, token;

    private Color color;

    private int position;

    public LobbyPlayer(String name, String token, Color color, int position) {
        this.name = name;
        this.token = token;
        this.color = color;
        this.position = position;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("name", name);
        out.put("color", color.toJsonObject());
        out.put("position", position);
        return out;
    }
}
