package de.lasse.risk_server.Lobby;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.lasse.risk_server.Database.Lobby.Color;
import de.lasse.risk_server.Database.Lobby.Lobby;
import de.lasse.risk_server.Database.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Database.Lobby.LobbyPlayer;
import de.lasse.risk_server.Database.Settings.ColorInterfaceRepository;

@RestController
@CrossOrigin
public class ColorController {

    @Autowired
    ColorInterfaceRepository colorInterfaceRepository;

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @RequestMapping(value = "settings/remainingcolors", method = RequestMethod.GET)
    public ResponseEntity<String> getRemainingColors(@RequestParam Optional<String> lobbyid) {
        if (!lobbyid.isPresent())
            return new ResponseEntity<String>("No lobbyid Provided", HttpStatus.BAD_REQUEST);

        Optional<Lobby> lobby = lobbyInterfaceRepository.findById(lobbyid.get());

        if (!lobby.isPresent())
            return new ResponseEntity<String>("Lobby not found", HttpStatus.NOT_FOUND);

        LobbyPlayer[] players = lobby.get().players;
        List<Color> allColors = colorInterfaceRepository.findAll();
        List<Color> remainingColors = new LinkedList<Color>();
        String hex;
        boolean exists;

        for (int i = 0; i < allColors.size(); i++) {
            exists = false;
            hex = allColors.get(i).hex;
            for (LobbyPlayer p : players) {
                if (hex.equalsIgnoreCase(p.color.hex)) {
                    exists = true;
                    break;
                }
            }

            if (!exists)
                remainingColors.add(allColors.get(i));
        }

        JSONArray out = new JSONArray();
        for (Color c : remainingColors)
            out.put(c.toJsonObject());
        return new ResponseEntity<String>(out.toString(), HttpStatus.ACCEPTED);
    }

}
