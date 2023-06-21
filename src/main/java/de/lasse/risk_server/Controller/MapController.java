package de.lasse.risk_server.Controller;

import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonView;

import de.lasse.risk_server.Database.Maps.DisplayMap;
import de.lasse.risk_server.Database.Maps.DisplayMapInterfaceRepository;
import de.lasse.risk_server.Database.Maps.Map;
import de.lasse.risk_server.Database.Maps.MapInterfaceRepository;
import de.lasse.risk_server.Database.Maps.Views;

@RestController
@CrossOrigin
public class MapController {

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    @Autowired
    DisplayMapInterfaceRepository displayMapInterfaceRepository;

    @RequestMapping(value = "/maps/{map_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getMap(@PathVariable String map_id) {
        try {
            Map map = mapInterfaceRepository.findMapById(map_id);
            return new ResponseEntity<String>(map.toJsonObject().toString(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("Couldnt find " + map_id);
            return new ResponseEntity<String>("Map not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/displaymaps/{map_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.BackgroundMapView.class)
    @ResponseBody
    public ResponseEntity<DisplayMap> getDisplayMap(@PathVariable String map_id) {
        try {
            DisplayMap map = displayMapInterfaceRepository.findDisplayMapById(map_id);
            return new ResponseEntity<DisplayMap>(map, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("Couldnt find " + map_id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/displaymaps/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.MiniatureMapView.class)
    @ResponseBody
    public ResponseEntity<List<DisplayMap>> getDisplayMaps() {
        return new ResponseEntity<List<DisplayMap>>(displayMapInterfaceRepository.findAll(), HttpStatus.ACCEPTED);
    }

}
