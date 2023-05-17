package de.lasse.risk_server.Database.Lobby;

import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "colors")
public class Color {

    public String name, hex;

    public Color(String name, String hex) {
        this.name = name;
        this.hex = hex;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("name", name);
        out.put("hex", hex);
        return out;
    }

}
