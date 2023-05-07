package de.lasse.risk_server.Database.Lobby;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

import nonapi.io.github.classgraph.json.Id;

@Document(collection = "lobbies")
public class Lobby {

    @Id
    private String gameId;

    private String mapId;

    private LobbyPlayer[] players;

    public JSONObject toJsonObject() {
        JSONObject out = new JSONObject();
        out.put("gameId", gameId);
        out.put("mapId", mapId);

        JSONArray playerArray = new JSONArray();
        for (LobbyPlayer player : players) {
            playerArray.put(player.toJsonObject());
        }

        out.put("players", playerArray);
        return out;
    }
}
