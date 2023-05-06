package de.lasse.risk_server.Database.Maps;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "maps")
public class Map {

    @Id
    private String id;

    private String name;

    private int width, height;

    private Territory[] territories;

    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("name", this.name);
        obj.put("width", this.width);
        obj.put("height", this.height);

        JSONArray jsonArray = new JSONArray();
        for (Territory territory : territories)
            jsonArray.put(territory.toJsonObject());
        obj.put("territories", jsonArray);
        return obj;
    }

}
