package de.lasse.risk_server.Game.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import de.lasse.risk_server.Game.Continent.Continent;

@Document(collection = "maps")
public class Map {

    @Id
    private String id;

    @Field("svg_width")
    private int svgWidth;

    @Field("svg_height")
    private int svgHeight;

    @Field("territories")
    private MapTerritory[] territories;

    @Field("continents")
    private Continent[] continents;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSvgWidth() {
        return this.svgWidth;
    }

    public void setSvgWidth(int svgWidth) {
        this.svgWidth = svgWidth;
    }

    public int getSvgHeight() {
        return this.svgHeight;
    }

    public void setSvgHeight(int svgHeight) {
        this.svgHeight = svgHeight;
    }

    public MapTerritory[] getTerritories() {
        return this.territories;
    }

    public void setTerritories(MapTerritory[] territories) {
        this.territories = territories;
    }

    public Continent[] getContinents() {
        return this.continents;
    }

    public void setContinents(Continent[] continents) {
        this.continents = continents;
    }

}
