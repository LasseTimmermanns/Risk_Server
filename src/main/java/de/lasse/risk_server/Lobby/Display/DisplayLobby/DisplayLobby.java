package de.lasse.risk_server.Lobby.Display.DisplayLobby;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "lobbies")
public class DisplayLobby {

    @Id
    private String id;

    @Field("player_count")
    private int playerCount;

    @Field("max_players")
    private int maxPlayers;

    @Field("host")
    private String host;

    @Field("turn_timer")
    private int turnTimer;

    @Field("card_bonus")
    private String cardBonus;

    @Field("map_id")
    private String mapId;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getTurnTimer() {
        return this.turnTimer;
    }

    public void setTurnTimer(int turnTimer) {
        this.turnTimer = turnTimer;
    }

    public String getCardBonus() {
        return this.cardBonus;
    }

    public void setCardBonus(String cardBonus) {
        this.cardBonus = cardBonus;
    }

    public String getMapId() {
        return this.mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

}
