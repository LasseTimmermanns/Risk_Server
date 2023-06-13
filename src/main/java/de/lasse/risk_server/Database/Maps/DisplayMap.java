package de.lasse.risk_server.Database.Maps;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "maps")
public class DisplayMap {

    @Id
    private String id;

    private String name;

    public int width, height;

    @Field("display_path")
    public String displayPath;

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();

        json.put("id", this.id);
        json.put("name", this.name);
        json.put("width", this.width);
        json.put("height", this.height);
        json.put("display_path", displayPath);
        return json;
    }
}
