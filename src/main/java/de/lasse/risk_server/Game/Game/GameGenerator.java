package de.lasse.risk_server.Game.Game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Game.Map.MapInterfaceRepository;
import de.lasse.risk_server.Game.Players.Player;
import de.lasse.risk_server.Game.Settings.SettingsState;
import de.lasse.risk_server.Game.Shape.ShapeInterfaceRepository;
import de.lasse.risk_server.Game.Territory.GameTerritory;
import de.lasse.risk_server.Lobby.Lobby.Lobby;
import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;
import de.lasse.risk_server.Shared.Color.Color;

@Service
public class GameGenerator {

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    @Autowired
    ShapeInterfaceRepository shapeInterfaceRepository;

    public Game generateGame(Lobby lobby) {
        String gameId = lobby.getId();
        String mapId = lobby.getMapId();
        int turn = 0, phase = 0;
        SettingsState settingsState = new SettingsState(lobby.isFixed());
        Player[] players = getPlayers(lobby.getPlayers());
        GameTerritory[] territories = generateTerritories(players, mapId);

        return new Game(gameId, mapId, players, territories, turn, phase, settingsState);
    }

    private List<Integer> generateRandomOrder(int range) {
        List<Integer> order = IntStream.range(0, range).boxed().collect(Collectors.toList());
        Collections.shuffle(order);
        return order;
    }

    private Player[] getPlayers(LobbyPlayer[] lobbyPlayers) {
        Player[] out = new Player[lobbyPlayers.length];
        List<Integer> order = generateRandomOrder(lobbyPlayers.length);

        for (int i = 0; i < lobbyPlayers.length; i++) {
            LobbyPlayer p = lobbyPlayers[order.get(i)];

            String id = p.getId();
            String token = p.getToken();
            String name = p.getName();
            Color color = p.getColor();
            int seat = order.get(i);
            int deploymentLeft = 0;

            out[i] = new Player(id, token, name, color, seat, new int[0], deploymentLeft);
        }

        Arrays.sort(out, (p1, p2) -> Integer.compare(p1.getSeat(), p2.getSeat()));
        return out;
    }

    private GameTerritory[] generateTerritories(Player[] players, String mapId) {
        int n = mapInterfaceRepository.findTerritoriesNum(mapId);
        GameTerritory[] territories = divideTerritories(players, n);

        int troopsPerPlayer = 50 - players.length * 5;

        for (Player p : players) {
            GameTerritory[] ownedTerritories = Arrays.stream(territories).filter(t -> t.getOwner().equals(p.getId()))
                    .toArray(GameTerritory[]::new);

            int troopsLeft = troopsPerPlayer - ownedTerritories.length;
            while (troopsLeft > 0) {
                int rand = (int) (Math.random() * ownedTerritories.length);
                ownedTerritories[rand].troops++;
                troopsLeft--;
            }
        }
        return territories;
    }

    private GameTerritory[] divideTerritories(Player[] players, int numTerritories) {
        GameTerritory[] territories = new GameTerritory[numTerritories];
        List<Integer> order = generateRandomOrder(numTerritories);
        for (int x = 0; x <= territories.length / players.length; x++) {
            for (int y = 0; y < players.length; y++) {
                int i = x * players.length + y;
                if (i >= numTerritories)
                    break;
                territories[order.get(i)] = new GameTerritory(order.get(i), 1, players[y].getId());
            }
        }

        return territories;
    }

}
