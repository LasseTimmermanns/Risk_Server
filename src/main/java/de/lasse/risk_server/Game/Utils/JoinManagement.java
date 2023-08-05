package de.lasse.risk_server.Game.Utils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import de.lasse.risk_server.Game.GameHandler;
import de.lasse.risk_server.Game.Game.Game;
import de.lasse.risk_server.Game.Game.GameInterfaceRepository;
import de.lasse.risk_server.Game.GameAction.GameActionHandler;
import de.lasse.risk_server.Game.Map.MapInterfaceRepository;
import de.lasse.risk_server.Game.Players.Player;
import de.lasse.risk_server.Game.Shape.ShapeInterfaceRepository;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.WebSocketHelper;

@Service
public class JoinManagement {

    @Autowired
    GameInterfaceRepository gameInterfaceRepository;

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    @Autowired
    ShapeInterfaceRepository shapeInterfaceRepository;

    @Autowired
    GameActionHandler gameActionHandler;

    public void join(QueryIdentification queryIdentification, WebSocketSession session) throws IOException {
        Optional<Game> gameOptional = gameInterfaceRepository.findById(queryIdentification.roomId);
        if (gameOptional.isEmpty()) {
            session.sendMessage(WebSocketHelper.generateDeclineMessage("game not found"));
            session.close();
            return;
        }

        Game game = gameOptional.get();
        for (Player player : game.getPlayers()) {
            if (!player.getToken().equals(queryIdentification.token))
                continue;
            GameHandler.sessions.get(queryIdentification.roomId).add(session);
            de.lasse.risk_server.Game.Map.Map map = mapInterfaceRepository.findById(game.getMapId()).orElseThrow();

            session.sendMessage(
                    WebSocketHelper.generateTextMessage("success",
                            Map.of("playerId", player.getId(), "game", game, "map", map)));

            return;
        }

        session.sendMessage(WebSocketHelper.generateDeclineMessage("token not valid"));
        session.close();
    }

}
