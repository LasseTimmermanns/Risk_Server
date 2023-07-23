package de.lasse.risk_server.Game.Shape;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shapes")
public class Shape {

    private String id;

    private int pos;

    private String path;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
