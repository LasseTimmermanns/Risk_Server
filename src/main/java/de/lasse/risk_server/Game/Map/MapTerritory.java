package de.lasse.risk_server.Game.Map;

import org.springframework.data.mongodb.core.mapping.Field;

public class MapTerritory {

    @Field("name")
    public String name;

    @Field("path")
    public String path;

    @Field("id")
    public int id;

    @Field("borders")
    public int[] borders;

    public MapTerritory(String name, String path, int id, int[] borders) {
        this.name = name;
        this.path = path;
        this.id = id;
        this.borders = borders;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getBorders() {
        return this.borders;
    }

    public void setBorders(int[] borders) {
        this.borders = borders;
    }

}
