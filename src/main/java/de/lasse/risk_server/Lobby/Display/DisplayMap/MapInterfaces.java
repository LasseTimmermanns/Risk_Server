package de.lasse.risk_server.Lobby.Display.DisplayMap;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MapInterfaces {

    public interface BackgroundMap {
        @Id
        public String getId();

        @JsonProperty("display_width")
        public int getDisplayWidth();

        @JsonProperty("display_height")
        public int getDisplayHeight();
    }

    public interface MiniatureMap {
        @Id
        public String getId();

        @JsonProperty("display_path")
        public String getDisplayPath();

        @JsonProperty("svg_width")
        public int getSvgWidth();

        @JsonProperty("svg_height")
        public int getSvgHeight();
    }

}
