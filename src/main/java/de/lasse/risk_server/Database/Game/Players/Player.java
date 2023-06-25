package de.lasse.risk_server.Database.Game.Players;

import org.springframework.data.annotation.Id;

import de.lasse.risk_server.Database.Lobby.Color;

public class Player {

    @Id
    public String id;

    public String token;

    public String name;

    public Color color;

    public int seat;

}
