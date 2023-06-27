package de.lasse.risk_server.Lobby.LobbyPlayer;

import org.springframework.data.annotation.Id;

import de.lasse.risk_server.Shared.Color.Color;

public class LobbyPlayer {

    @Id
    private String id;

    private String name, token;

    private Color color;

    private boolean host;

    private int position;

    private double flagx, flagy;

    public LobbyPlayer(String id, String name, String token, boolean host, Color color, int position, double flagx,
            double flagy) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.host = host;
        this.color = color;
        this.position = position;
        this.flagx = flagx;
        this.flagy = flagy;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isHost() {
        return this.host;
    }

    public boolean getHost() {
        return this.host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getFlagx() {
        return this.flagx;
    }

    public void setFlagx(double flagx) {
        this.flagx = flagx;
    }

    public double getFlagy() {
        return this.flagy;
    }

    public void setFlagy(double flagy) {
        this.flagy = flagy;
    }

}
