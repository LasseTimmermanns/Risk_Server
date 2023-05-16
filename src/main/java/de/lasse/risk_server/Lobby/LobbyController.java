package de.lasse.risk_server.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.lasse.risk_server.Database.DisplayLobby.DisplayLobby;
import de.lasse.risk_server.Database.DisplayLobby.DisplayLobbyRepository;
import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;
import de.lasse.risk_server.Utils.TokenGenerator;

@RestController
@CrossOrigin
public class LobbyController {

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @Autowired
    DisplayLobbyRepository displayLobbyRepository;

    @RequestMapping(value = "/lobbies/getall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getLobbies() {
        JSONArray out = new JSONArray();
        List<DisplayLobby> lobbies = displayLobbyRepository.getAll();
        for (DisplayLobby lobby : lobbies)
            out.put(lobby.toJsonObject());

        return new ResponseEntity<String>(out.toString(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/lobbies/get/{game_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getLobby(@PathVariable String game_id) {
        Optional<Lobby> lobby = lobbyInterfaceRepository.findById(game_id);

        if (lobby.isPresent())
            return new ResponseEntity<String>(lobby.get().toJsonObject().toString(), HttpStatus.ACCEPTED);

        System.out.println("No Lobby found with GameId=" + game_id);
        return new ResponseEntity<String>("Lobby not Found", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/lobbies/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createLobby() {
        Lobby l = Lobby.generateDefault();
        lobbyInterfaceRepository.save(l);

        LobbyHandler.sessions.put(l.id, new ArrayList<>());
        return new ResponseEntity<String>(l.id, HttpStatus.ACCEPTED);
    }

}
