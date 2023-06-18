package de.lasse.risk_server.Database.Maps;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "maps")
public class DisplayMatrixAllowed {

    public boolean value;

}
