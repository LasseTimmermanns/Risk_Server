package de.lasse.risk_server.Lobby.Color;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "colors")
public class Color implements Serializable {

    private String name, hex;

    @Field("text_color")
    @JsonProperty("text_color")
    private String textColor;

    public Color(String name, String hex, String textColor) {
        this.name = name;
        this.hex = hex;
        this.textColor = textColor;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

}
