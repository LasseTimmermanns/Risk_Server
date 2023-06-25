package de.lasse.risk_server.Database.Game.Territory;

import org.springframework.data.mongodb.core.mapping.Field;

public class GameTerritory {

    @Field("territory_id")
    private int territoryId;

    private int troops;

    private int owner;

    public int getTerritoryId() {
        return this.territoryId;
    }

    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }

    public int getTroops() {
        return this.troops;
    }

    public void setTroops(int troops) {
        this.troops = troops;
    }

    public int getOwner() {
        return this.owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

}
