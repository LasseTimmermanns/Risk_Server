package de.lasse.risk_server.MapProcessing;

import org.json.JSONArray;
import org.json.JSONObject;

public class Map {

    private Territory[] territories;
    private JSONArray borders;
    private int width;
    private int height;

    public Map(Territory[] territories, JSONArray borders, int width, int height) {
        this.territories = territories;
        this.borders = borders;
        this.width = width;
        this.height = height;
    }

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("width", width);
        out.put("height", height);

        JSONArray territoryArray = new JSONArray();

        for (Territory t : territories) {
            territoryArray.put(t.toJson());
        }

        out.put("territories", territoryArray);
        out.put("borders", borders);
        return out;
    }

}
