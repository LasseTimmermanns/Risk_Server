package de.lasse.risk_server.Lobby.Display.DisplayMatrix;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "maps")
public class DisplayMatrixAllowed {

    public boolean value;

}
