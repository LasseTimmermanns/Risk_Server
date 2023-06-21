package de.lasse.risk_server.Database.Maps;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonView;

@Document(collection = "maps")
public class DisplayMap {

    @Id
    @JsonView(Views.DisplayMapView.class)
    public String id;

    @Field("display_width")
    @JsonView(Views.BackgroundMapView.class)
    public int display_width;

    @Field("display_height")
    @JsonView(Views.BackgroundMapView.class)
    public int display_height;

    @Field("display_path")
    @JsonView(Views.MiniatureMapView.class)
    public String path;

    @Field("width")
    @JsonView(Views.MiniatureMapView.class)
    public int svg_width;

    @Field("height")
    @JsonView(Views.MiniatureMapView.class)
    public int svg_height;

}
