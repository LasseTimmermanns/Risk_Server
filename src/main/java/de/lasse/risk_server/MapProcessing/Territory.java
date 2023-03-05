package de.lasse.risk_server.MapProcessing;

import org.json.JSONObject;

public class Territory {

    private int id;
    private String name;
    private String path;

    public Territory(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("name", name);
        out.put("path", path);
        return out;
    }
}
