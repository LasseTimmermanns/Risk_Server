package de.lasse.risk_server.Database.Game;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import de.lasse.risk_server.Database.Game.Players.Player;
import de.lasse.risk_server.Database.Game.Territory.GameTerritory;

@Document(collection = "games")
public class Game {

    @Id
    public String id;

    @Field("map_id")
    public String mapId;

    @Field("players")
    public Player[] players;

    @Field("territories")
    public GameTerritory[] territories;

    @Field("move")
    public int move;

    @Field("settings")
    public SettingsObj settings;

}
