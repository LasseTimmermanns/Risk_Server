package de.lasse.risk_server.Database.Maps;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "maps")
public class DisplayMap {

    @Id
    public String id;

    @Field("display_width")
    public int width;

    @Field("display_height")
    public int height;

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();

        json.put("id", this.id);
        json.put("width", this.width);
        json.put("height", this.height);
        return json;
    }
}
