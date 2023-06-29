package de.lasse.risk_server.Lobby.Display.DisplayMap;

import org.springframework.data.annotation.Id;

public class MapInterfaces {

    public interface BackgroundMap {
        @Id
        public String getId();

        public int getDisplayWidth();

        public int getDisplayHeight();
    }

    public interface MiniatureMap {
        @Id
        public String getId();

        public String getDisplayPath();

        public int getSvgWidth();

        public int getSvgHeight();
    }

}
