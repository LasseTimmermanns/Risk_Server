package de.lasse.risk_server.Lobby.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.lasse.risk_server.Lobby.Lobby.Lobby;
import de.lasse.risk_server.Lobby.Lobby.LobbyInterfaceRepository;
import de.lasse.risk_server.Lobby.LobbyPlayer.LobbyPlayer;
import de.lasse.risk_server.Lobby.Settings.PlayerSettingsService;

@RestController
@CrossOrigin
public class ColorController {

    @Autowired
    ColorInterfaceRepository colorInterfaceRepository;

    @Autowired
    LobbyInterfaceRepository lobbyInterfaceRepository;

    @RequestMapping(value = "settings/colors/remaining", method = RequestMethod.GET)
    public ResponseEntity<List<Color>> getRemainingColors(@RequestParam Optional<String> lobbyid) {
        if (!lobbyid.isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Lobby> lobby = lobbyInterfaceRepository.findById(lobbyid.get());

        if (!lobby.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        LobbyPlayer[] players = lobby.get().getPlayers();
        List<Color> allColors = colorInterfaceRepository.findAll();
        List<Color> remainingColors = new LinkedList<Color>();
        String hex;
        boolean exists;

        for (int i = 0; i < allColors.size(); i++) {
            exists = false;
            hex = allColors.get(i).getHex();
            for (LobbyPlayer p : players) {
                if (hex.equalsIgnoreCase(p.getColor().getHex())) {
                    exists = true;
                    break;
                }
            }

            if (!exists)
                remainingColors.add(allColors.get(i));
        }

        return new ResponseEntity<List<Color>>(remainingColors, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "settings/colors/all", method = RequestMethod.GET)
    public ResponseEntity<List<Color>> getAllColors() {
        return new ResponseEntity<List<Color>>(PlayerSettingsService.getColors(), HttpStatus.ACCEPTED);
    }

}
