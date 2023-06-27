package de.lasse.risk_server.Game.Game;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import de.lasse.risk_server.Game.Players.Player;
import de.lasse.risk_server.Game.Settings.SettingsState;
import de.lasse.risk_server.Game.Territory.GameTerritory;

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
    private SettingsState settings;

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

    public SettingsState getSettings() {
        return this.settings;
    }

    public void setSettings(SettingsState settings) {
        this.settings = settings;
    }

    public Game(String id, String mapId, Player[] players, GameTerritory[] territories, int move,
            SettingsState settings) {
        this.id = id;
        this.mapId = mapId;
        this.players = players;
        this.territories = territories;
        this.move = move;
        this.settings = settings;
    }

}
