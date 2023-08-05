package de.lasse.risk_server.Game.GameAction;

public class Deployment {

    public int territoryId;
    public int amount;

    public int getTerritoryId() {
        return this.territoryId;
    }

    public void setTerritoryId(int toId) {
        this.territoryId = toId;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
