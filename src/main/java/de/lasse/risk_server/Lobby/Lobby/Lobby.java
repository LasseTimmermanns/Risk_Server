package de.lasse.risk_server.Lobby.Lobby;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;

@Document(collection = "lobbies")
public class Lobby {

    @Id
    private String id;

    @Field("max_players")
    private int maxPlayers;

    @Field("turn_timer")
    private int turnTimer;

    @Field("is_fixed")
    private boolean isFixed;

    @Field("map_id")
    private String mapId;

    @Field("creation_date")
    private long creationDate;

    @Field("players")
    private LobbyPlayer[] players;

    @Field("is_public")
    private boolean isPublic;

    public Lobby(int maxPlayers, int turnTimer, boolean isFixed, String mapId, long creationDate,
            LobbyPlayer[] players, boolean isPublic) {
        this.maxPlayers = maxPlayers;
        this.turnTimer = turnTimer;
        this.isFixed = isFixed;
        this.mapId = mapId;
        this.players = players;
        this.creationDate = creationDate;
        this.isPublic = isPublic;
    }

    public static Lobby generateDefault() {
        Lobby out = new Lobby(6, 60, true, "classic", System.currentTimeMillis(), new LobbyPlayer[0], false);
        return out;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int max_players) {
        this.maxPlayers = max_players;
    }

    public int getTurnTimer() {
        return this.turnTimer;
    }

    public void setTurnTimer(int turn_timer) {
        this.turnTimer = turn_timer;
    }

    public boolean isFixed() {
        return this.isFixed;
    }

    public void setFixed(boolean is_fixed) {
        this.isFixed = is_fixed;
    }

    public String getMapId() {
        return this.mapId;
    }

    public void setMapId(String map_id) {
        this.mapId = map_id;
    }

    public long getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(long creation_date) {
        this.creationDate = creation_date;
    }

    public LobbyPlayer[] getPlayers() {
        return this.players;
    }

    public void setPlayers(LobbyPlayer[] players) {
        this.players = players;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public void setPublic(boolean is_public) {
        this.isPublic = is_public;
    }

}
