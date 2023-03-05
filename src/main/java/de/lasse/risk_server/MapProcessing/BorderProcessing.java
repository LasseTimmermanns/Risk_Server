package de.lasse.risk_server.MapProcessing;

import org.json.JSONArray;

public class BorderProcessing {

    public static JSONArray getBorders(String json, Territory[] territories) {
        for (Territory t : territories) {
            json = json.replaceAll(t.getName().toLowerCase(),
                    Integer.toString(t.getId()));
        }

        return new JSONArray(json);
    }

}
