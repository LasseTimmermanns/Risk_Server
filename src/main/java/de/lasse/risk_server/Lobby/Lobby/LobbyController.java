package de.lasse.risk_server.Lobby.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import de.lasse.risk_server.Lobby.LobbyHandler;
import de.lasse.risk_server.Lobby.Display.DisplayLobby.DisplayLobby;
import de.lasse.risk_server.Lobby.Display.DisplayLobby.DisplayLobbyRepository;

@RestController
@CrossOrigin
public class LobbyController {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    DisplayLobbyRepository displayLobbyRepository;

    @RequestMapping(value = "/lobbies/getall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<DisplayLobby>> getLobbies() {
        List<DisplayLobby> lobbies = displayLobbyRepository.getAll();
        return new ResponseEntity<List<DisplayLobby>>(lobbies, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/lobbies/get/{game_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Lobby> getLobby(@PathVariable String game_id) {
        Optional<Lobby> lobby = lobbyInterfaceRepository.findById(game_id);

        if (lobby.isPresent())
            return new ResponseEntity<Lobby>(lobby.get(), HttpStatus.ACCEPTED);

        System.out.println("No Lobby found with gameId:" + game_id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/lobbies/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, String>> createLobby() {
        Lobby l = Lobby.generateDefault();
        lobbyInterfaceRepository.save(l);

        LobbyHandler.sessions.put(l.getId(), new ArrayList<>());
        return new ResponseEntity<Map<String, String>>(Map.of("lobbyId", l.getId()), HttpStatus.ACCEPTED);
    }

}
