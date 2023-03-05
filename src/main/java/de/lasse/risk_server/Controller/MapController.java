package de.lasse.risk_server.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.lasse.risk_server.MapProcessing.MapProcessing;

@RestController
@CrossOrigin
public class MapController {

    @RequestMapping(value = "/maps/{map_name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getMap(@PathVariable String map_name) {
        String body = MapProcessing.createMapObject(map_name).toJson().toString();
        return new ResponseEntity<String>(body, HttpStatus.ACCEPTED);
    }

}
