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
import de.lasse.risk_server.Game.Game.Game;
import de.lasse.risk_server.Game.Game.GameInterfaceRepository;
import de.lasse.risk_server.Game.Players.Player;
import de.lasse.risk_server.Game.Territory.GameTerritory;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.WebSocketHelper;

@Service
public class GameActionHandler {

    @Autowired
    GameInterfaceRepository gameInterfaceRepository;

    public void action(JsonNode json, QueryIdentification queryIdentification, WebSocketSession session) {
        Game game = getGame(queryIdentification.roomId);
        Player player = getPlayer(queryIdentification.token, game);

        try {
            switch (json.get("action").asText()) {
                case "deploy":
                    Deployment deployment = new ObjectMapper().treeToValue(json.get("actionData"), Deployment.class);
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

    private boolean playerOnTurn(Player player, Game game) {
        return game.getTurn() % game.getPlayers().length == player.getSeat();
    }

    private void deployTroops(Game game, Player player, Deployment deployment) {
        if (player.getDeploymentLeft() == 0)
            return;

        if (!playerOnTurn(player, game))
            return;

        if (!rightPhase(game, 0))
            return;

        if (player.getDeploymentLeft() < deployment.amount)
            deployment.amount = player.getDeploymentLeft();

        GameTerritory territory = findGameTerritoryById(game.getTerritories(), deployment.getToId()).orElseThrow();

        if (!territory.getOwner().equals(player.getId()))
            return;

        player.setDeploymentLeft(player.getDeploymentLeft() - deployment.amount);

        territory.setTroops(territory.getTroops() + deployment.amount);

        gameInterfaceRepository.save(game);

        TextMessage message = WebSocketHelper.generateGameActionMessage("deploy",
                Map.of("toId", deployment.getToId(), "amount", deployment.amount));

        GameHandler.broadcast(message, game.getId());
    }

    private void nextPhase(Game game, Player player) {
        if (!playerOnTurn(player, game))
            return;

        int phase = game.getPhase();
        phase++;

        TextMessage message;
        if (phase < 3) {
            game.setPhase(phase);
            message = WebSocketHelper.generateGameActionMessage("nextPhase");
        } else {
            game.setTurn(game.getTurn() + 1);
            message = WebSocketHelper.generateGameActionMessage("nextTurn");
        }

        gameInterfaceRepository.save(game);
        GameHandler.broadcast(message, game.getId());
    }

}
