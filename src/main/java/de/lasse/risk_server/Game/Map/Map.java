package de.lasse.risk_server.Game.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "maps")
public class Map {

    @Id
    private String id;

    private int width, height;

    private MapTerritory[] territories;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public MapTerritory[] getTerritories() {
        return this.territories;
    }

    public void setTerritories(MapTerritory[] territories) {
        this.territories = territories;
    }

}
