package de.lasse.risk_server.Game.Players;

import org.springframework.data.annotation.Id;

import de.lasse.risk_server.Shared.Color.Color;

public class Player {

    @Id
    private String id;

    private String token;

    private String name;

    private Color color;

    private int seat;

    private int[] cards;

    public int[] getCards() {
        return this.cards;
    }

    public void setCards(int[] cards) {
        this.cards = cards;
    }

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

    public Player(String id, String token, String name, Color color, int seat, int[] cards) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.color = color;
        this.seat = seat;
        this.cards = cards;
    }

}
