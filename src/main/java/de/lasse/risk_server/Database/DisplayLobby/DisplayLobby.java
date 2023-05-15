package de.lasse.risk_server.Database.DisplayLobby;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lobbies")
public class DisplayLobby {

    @Id
    private String id;

    private int playerCount;

    private int maxPlayers;

    private String host;

    private int turnTimer;

    private String cardBonus;

    private String mapId;

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("id", id);
        out.put("mapId", mapId);
        out.put("turnTimer", turnTimer);
        out.put("cardBonus", cardBonus);
        out.put("playerCount", playerCount);
        out.put("maxPlayers", maxPlayers);
        out.put("host", host);
        return out;
    }
}
