package de.lasse.risk_server.Database.Settings;

import org.json.JSONObject;

public class Color {

    private String name, hex;

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("name", name);
        out.put("hex", hex);
        return out;
    }

}
