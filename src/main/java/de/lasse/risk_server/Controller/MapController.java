package de.lasse.risk_server.Controller;

import java.io.IOException;

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

import de.lasse.risk_server.Database.Maps.Map;
import de.lasse.risk_server.Database.Maps.MapInterfaceRepository;
import de.lasse.risk_server.Utils.FileUtils;

@RestController
@CrossOrigin
public class MapController {

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    @RequestMapping(value = "/maps/{map_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getMap(@PathVariable String map_name) {
        try {
            Map map = mapInterfaceRepository.findMapByName("classic");
            return new ResponseEntity<String>(map.toJsonObject().toString(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("Couldnt find " + map_name);
            return new ResponseEntity<String>("Map not found", HttpStatus.NOT_FOUND);
        }
    }

}
