package de.lasse.risk_server.Database.Lobby;

import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "colors")
public class Color {

    public String name, hex;

    @Field("text_color")
    public String textColor;

    public Color(String name, String hex, String textColor) {
        this.name = name;
        this.hex = hex;
        this.textColor = textColor;
    }

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("name", name);
        out.put("hex", hex);
        out.put("text_color", textColor);
        return out;
    }

}
