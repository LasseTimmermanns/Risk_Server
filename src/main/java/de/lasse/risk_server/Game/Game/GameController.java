package de.lasse.risk_server.Game.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.lasse.risk_server.Game.Map.MapInterfaceRepository;

@RestController
@CrossOrigin
public class GameController {

    @Autowired
    GameInterfaceRepository gameInterfaceRepository;

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    @RequestMapping(value = "/game/example", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Game> getExampleGame() {

        try {
            Game game = gameInterfaceRepository.findAll().get(0);
            return new ResponseEntity<Game>(game, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.err.println("NO GAME AVAILABLE FOR EXAMPLE CONTROLLER");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
