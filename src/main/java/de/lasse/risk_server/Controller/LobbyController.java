package de.lasse.risk_server.Controller;

import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;

@RestController
@CrossOrigin
public class LobbyController {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @RequestMapping(value = "/lobbies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getLobbies() {
        JSONArray out = new JSONArray();
        List<Lobby> lobbies = lobbyInterfaceRepository.findAll();
        for (Lobby lobby : lobbies)
            out.put(lobby.toJsonObject());

        return new ResponseEntity<String>(out.toString(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/lobbies/{game_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getLobby(@PathVariable String game_id) {
        Lobby lobby = lobbyInterfaceRepository.findByGameId(game_id);

        if (lobby != null)
            return new ResponseEntity<String>(lobby.toJsonObject().toString(), HttpStatus.ACCEPTED);

        System.out.println("No Lobby found with GameId=" + game_id);
        return new ResponseEntity<String>("Lobby not Found", HttpStatus.NOT_FOUND);
    }

}
