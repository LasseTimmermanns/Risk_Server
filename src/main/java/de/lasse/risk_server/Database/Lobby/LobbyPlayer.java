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

    public double flagx, flagy;

    public LobbyPlayer(String id, String name, String token, boolean host, Color color, int position, double flagx,
            double flagy) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.host = host;
        this.color = color;
        this.position = position;
        this.flagx = flagx;
        this.flagy = flagy;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("name", name);
        out.put("host", host);
        out.put("color", color.toJsonObject());
        out.put("position", position);
        out.put("flagx", flagx);
        out.put("flagy", flagy);
        return out;
    }
}
