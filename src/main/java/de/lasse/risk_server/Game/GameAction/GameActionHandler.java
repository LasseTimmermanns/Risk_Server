package de.lasse.risk_server.Game.GameAction;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.lasse.risk_server.Game.GameHandler;
import de.lasse.risk_server.Game.Continent.Continent;
import de.lasse.risk_server.Game.Game.Game;
import de.lasse.risk_server.Game.Game.GameInterfaceRepository;
import de.lasse.risk_server.Game.Map.MapInterfaceRepository;
import de.lasse.risk_server.Game.Players.Player;
import de.lasse.risk_server.Game.Territory.GameTerritory;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.WebSocketHelper;

@Service
public class GameActionHandler {

    @Autowired
    GameInterfaceRepository gameInterfaceRepository;

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    public void action(JsonNode json, QueryIdentification queryIdentification, WebSocketSession session) {
        Game game = getGame(queryIdentification.roomId);
        Player player = getPlayer(queryIdentification.token, game);

        System.out.println("Game Action");

        try {
            switch (json.get("action").asText()) {
                case "deploy":
                    Deployment deployment = new ObjectMapper().treeToValue(json.get("actiondata"), Deployment.class);
                    deployTroops(game, player, deployment);
                    break;
                case "nextPhase":
                    nextPhase(game, player);
                    break;
            }

        } catch (JsonProcessingException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private Optional<GameTerritory> findGameTerritoryById(GameTerritory[] territories, int targetId) {
        return Arrays.stream(territories)
                .filter(t -> t.getId() == targetId)
                .findFirst();
    }

    private Game getGame(String gameId) {
        return this.gameInterfaceRepository.findById(gameId).orElseThrow();
    }

    private Player getPlayer(String playerToken, Game game) {
        return Arrays.stream(game.getPlayers()).filter(player -> player.getToken().equals(playerToken)).findFirst()
                .orElseThrow();
    }

    private boolean rightPhase(Game game, int desiredPhase) {
        return game.getPhase() == desiredPhase;
    }

    private boolean isPlayerOnTurn(Player player, Game game) {
        return getSeatOnTurn(game) == player.getSeat();
    }

    private int getSeatOnTurn(Game game) {
        return game.getTurn() % game.getPlayers().length;
    }

    private void deployTroops(Game game, Player player, Deployment deployment) {
        if (player.getDeploymentLeft() < 1) {
            System.out.println("Player has no Deployment left");
            return;
        }

        if (!isPlayerOnTurn(player, game)) {
            System.out.println("Deployment Player is not on Turn");
            return;
        }

        if (!rightPhase(game, 0)) {
            System.out.println("Its not Deployment Phase");
            return;
        }

        if (player.getDeploymentLeft() < deployment.amount) {
            System.out.println("Player has not enough Deployment left");
            deployment.amount = player.getDeploymentLeft();
        }

        GameTerritory territory = findGameTerritoryById(game.getTerritories(), deployment.getTerritoryId())
                .orElseThrow();

        if (!territory.getOwner().equals(player.getId())) {
            System.out.println("Player is not owner of Territory");
            return;
        }

        player.setDeploymentLeft(player.getDeploymentLeft() - deployment.amount);

        territory.setTroops(territory.getTroops() + deployment.amount);

        gameInterfaceRepository.save(game);

        TextMessage message = WebSocketHelper.generateGameActionMessage("deploy",
                Map.of("territoryId", deployment.getTerritoryId(), "amount", deployment.amount));

        GameHandler.broadcast(message, game.getId());
    }

    private void nextPhase(Game game, Player player) {
        if (!isPlayerOnTurn(player, game))
            return;

        int phase = game.getPhase();
        phase++;

        TextMessage message;
        if (phase < 3) {
            game.setPhase(phase);
            message = WebSocketHelper.generateGameActionMessage("nextPhase");
            switch (phase) {
                case 0:
                    beginDeployment(game);
                    break;
            }
        } else {
            game.setPhase(0);
            game.setTurn(game.getTurn() + 1);
            beginDeployment(game);
            message = WebSocketHelper.generateGameActionMessage("nextTurn");
        }

        gameInterfaceRepository.save(game);
        GameHandler.broadcast(message, game.getId());
    }

    private Optional<Player> getPlayerOnTurn(Game game) {
        int seatOnTurn = getSeatOnTurn(game);
        return Arrays.stream(game.getPlayers()).filter(player -> player.getSeat() == seatOnTurn).findFirst();
    }

    public void beginDeployment(Game game) {
        Player playerOnTurn = getPlayerOnTurn(game).orElseThrow();
        int deploymentLeft = 0;

        // Territories
        deploymentLeft = (int) Arrays.stream(game.getTerritories())
                .filter(t -> t.getOwner().equals(playerOnTurn.getId())).count() / 3;
        deploymentLeft = deploymentLeft < 3 ? 3 : deploymentLeft;

        // Bonuses
        de.lasse.risk_server.Game.Map.Map map = mapInterfaceRepository.findById(game.getMapId()).orElseThrow();
        for (Continent c : map.getContinents()) {
            boolean possesesContinent = true;
            for (int territoryId : c.getTerritories()) {
                GameTerritory territory = Arrays.stream(game.getTerritories())
                        .filter(t -> t.id == territoryId).findFirst().orElseThrow();
                if (!territory.owner.equals(playerOnTurn.getId())) {
                    possesesContinent = false;
                    break;
                }
            }
            if (!possesesContinent)
                continue;

            deploymentLeft += c.bonus;
        }

        playerOnTurn.setDeploymentLeft(deploymentLeft);
        gameInterfaceRepository.save(game);

        GameHandler.broadcast(WebSocketHelper.generateGameActionMessage("beginDeployment",
                Map.of("playerId", playerOnTurn.getId(), "amount", deploymentLeft)), game.getId());
    }

}
