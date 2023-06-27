package de.lasse.risk_server.Game.Territory;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface GameTerritory {

    @Field("territory_id")
    @JsonProperty("territory_id")
    public int territoryId();

    public int troops();

    public int owner();

}
