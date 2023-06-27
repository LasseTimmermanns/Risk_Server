package de.lasse.risk_server.Lobby.Display.DisplayMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "maps")
public class DisplayMap {

    @Id
    private String id;

    @Field("display_width")
    private int displayWidth;

    @Field("display_height")
    private int displayHeight;

    @Field("display_path")
    private String displayPath;

    @Field("svg_width")
    private int svgWidth;

    @Field("svg_height")
    private int svgHeight;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDisplayWidth() {
        return this.displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return this.displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public String getDisplayPath() {
        return this.displayPath;
    }

    public void setDisplayPath(String path) {
        this.displayPath = path;
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

}
