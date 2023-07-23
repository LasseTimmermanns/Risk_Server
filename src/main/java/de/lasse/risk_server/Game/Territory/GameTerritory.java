package de.lasse.risk_server.Game.Territory;

public class GameTerritory {

    public int id;

    public int troops;

    public String owner;

    public GameTerritory(int id, int troops, String owner) {
        this.id = id;
        this.troops = troops;
        this.owner = owner;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTroops() {
        return this.troops;
    }

    public void setTroops(int troops) {
        this.troops = troops;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
