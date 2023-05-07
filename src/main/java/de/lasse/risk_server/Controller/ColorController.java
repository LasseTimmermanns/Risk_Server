package de.lasse.risk_server.Controller;

import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.lasse.risk_server.Database.Settings.Color;
import de.lasse.risk_server.Database.Settings.ColorInterfaceRepository;

@RestController
@CrossOrigin
public class ColorController {

    @Autowired
    ColorInterfaceRepository colorInterfaceRepository;

    @RequestMapping(value = "/settings/colors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getLobbies() {
        JSONArray out = new JSONArray();
        List<Color> colors = colorInterfaceRepository.findAll();
        for (Color color : colors)
            out.put(color.toJsonObject());

        return new ResponseEntity<String>(out.toString(), HttpStatus.ACCEPTED);
    }

}
