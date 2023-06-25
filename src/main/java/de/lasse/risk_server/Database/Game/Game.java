package de.lasse.risk_server.Database.Game;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import de.lasse.risk_server.Database.Game.Players.Player;
import de.lasse.risk_server.Database.Game.Territory.GameTerritory;

@Document(collection = "games")
public class Game {

    @Id
    private String id;

    @Field("map_id")
    private String mapId;

    @Field("players")
    private Player[] players;

    @Field("territories")
    public GameTerritory[] territories;

    @Field("move")
    private int move;

    @Field("settings")
    private SettingsObj settings;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapId() {
        return this.mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public GameTerritory[] getTerritories() {
        return this.territories;
    }

    public void setTerritories(GameTerritory[] territories) {
        this.territories = territories;
    }

    public int getMove() {
        return this.move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public SettingsObj getSettings() {
        return this.settings;
    }

    public void setSettings(SettingsObj settings) {
        this.settings = settings;
    }

}
