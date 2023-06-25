package de.lasse.risk_server.Game.Map;

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

@RestController
@CrossOrigin
public class MapController {

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    @RequestMapping(value = "/maps/{map_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map> getMap(@PathVariable String map_id) {
        try {
            Map map = mapInterfaceRepository.findMapById(map_id);
            return new ResponseEntity<Map>(map, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("Couldnt find " + map_id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
