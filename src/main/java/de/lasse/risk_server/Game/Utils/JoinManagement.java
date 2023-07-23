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
import de.lasse.risk_server.Game.Players.Player;
import de.lasse.risk_server.Shared.QueryIdentification;
import de.lasse.risk_server.Shared.WebSocketHelper;

@Service
public class JoinManagement {

    @Autowired
    GameInterfaceRepository gameInterfaceRepository;

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
            session.sendMessage(WebSocketHelper.generateTextMessage("playerId", Map.of("playerId", player.getId())));
            session.sendMessage(WebSocketHelper.generateTextMessage("success", game));
            return;
        }

        session.sendMessage(WebSocketHelper.generateDeclineMessage("token not valid"));
        session.close();
    }

}
