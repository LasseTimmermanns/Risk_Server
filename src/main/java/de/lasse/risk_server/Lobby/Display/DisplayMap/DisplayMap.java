package de.lasse.risk_server.Lobby.Display.DisplayMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Document(collection = "maps")
public class DisplayMap {

    @Id
    @JsonView(Views.DisplayMapView.class)
    private String id;

    @Field("display_width")
    @JsonProperty("display_width")
    @JsonView(Views.BackgroundMapView.class)
    private int displayWidth;

    @Field("display_height")
    @JsonProperty("display_height")
    @JsonView(Views.BackgroundMapView.class)
    private int displayHeight;

    @Field("display_path")
    @JsonProperty("display_path")
    @JsonView(Views.MiniatureMapView.class)
    private String path;

    @Field("svg_width")
    @JsonProperty("svg_width")
    @JsonView(Views.MiniatureMapView.class)
    private int svgWidth;

    @Field("svg_height")
    @JsonProperty("svg_height")
    @JsonView(Views.MiniatureMapView.class)
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

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
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
