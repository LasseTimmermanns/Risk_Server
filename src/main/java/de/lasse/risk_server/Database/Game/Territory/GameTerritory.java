package de.lasse.risk_server.Database.Game.Territory;

import org.springframework.data.mongodb.core.mapping.Field;

public class GameTerritory {

    @Field("territory_id")
    public int territoryId;

    public int troops;

    public int owner;

}
