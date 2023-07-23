package de.lasse.risk_server.Game.Continent;

import org.springframework.data.mongodb.core.mapping.Field;

public class Continent {

    public String name;
    public int bonus;
    public String hex;
    public int[] territories;

    @Field("center_x")
    public int centerX;

    @Field("center_y")
    public int centerY;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBonus() {
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int[] getTerritories() {
        return this.territories;
    }

    public void setTerritories(int[] territories) {
        this.territories = territories;
    }

    public String getHex() {
        return this.hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

}
