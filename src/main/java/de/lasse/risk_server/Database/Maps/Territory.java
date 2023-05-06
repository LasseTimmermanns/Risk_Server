package de.lasse.risk_server.Database.Maps;

import org.json.JSONObject;

public class Territory {

    private String name;

    private String path;

    private int id;

    private int[] borders;

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.name);
        jsonObject.put("path", this.path);
        jsonObject.put("id", this.id);
        jsonObject.put("borders", this.borders);
        return jsonObject;
    }
}
