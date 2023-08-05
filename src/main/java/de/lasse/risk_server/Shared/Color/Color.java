package de.lasse.risk_server.Shared.Color;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import nonapi.io.github.classgraph.json.Id;

@Document(collection = "colors")
public class Color implements Serializable {

    @Id
    private String id;

    private String hex;

    @Field("text_color")
    private String textColor;

    @Field("secondary_hex")
    private String secondaryHex;

    @Field("highlight_hex")
    private String highlightHex;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHex() {
        return this.hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getSecondaryHex() {
        return this.secondaryHex;
    }

    public void setSecondaryHex(String secondaryHex) {
        this.secondaryHex = secondaryHex;
    }

    public String getHighlightHex() {
        return this.highlightHex;
    }

    public void setHighlightHex(String highlightHex) {
        this.highlightHex = highlightHex;
    }

}
