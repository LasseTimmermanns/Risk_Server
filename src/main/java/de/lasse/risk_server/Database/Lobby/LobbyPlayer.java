package de.lasse.risk_server.Database.Lobby;

import org.json.JSONObject;

import nonapi.io.github.classgraph.json.Id;

public class LobbyPlayer {

    @Id
    public String id;

    public String name, token;

    public Color color;

    public boolean host;

    public int position;

    public LobbyPlayer(String id, String name, String token, boolean host, Color color, int position) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.host = host;
        this.color = color;
        this.position = position;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("name", name);
        out.put("host", host);
        out.put("color", color.toJsonObject());
        out.put("position", position);
        return out;
    }
}
