package de.lasse.risk_server.Game.Players;

import org.springframework.data.annotation.Id;

import de.lasse.risk_server.Lobby.Color.Color;

public class Player {

    @Id
    private String id;

    private String token;

    private String name;

    private Color color;

    private int seat;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSeat() {
        return this.seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

}
