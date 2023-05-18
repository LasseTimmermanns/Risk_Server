package de.lasse.risk_server.Database.Lobby;

import org.json.JSONObject;

import nonapi.io.github.classgraph.json.Id;

public class LobbyPlayer {

    @Id
    public String id;

    public String name, token;

    public Color color;

    public int position;

    public LobbyPlayer(String id, String name, String token, Color color, int position) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.color = color;
        this.position = position;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("name", name);
        out.put("color", color.toJsonObject());
        out.put("position", position);
        return out;
    }
}
