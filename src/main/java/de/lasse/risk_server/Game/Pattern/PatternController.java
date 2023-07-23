package de.lasse.risk_server.Game.Pattern;

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

@CrossOrigin
@RestController
public class PatternController {

    @Autowired
    PatternInterfaceRepository patternInterfaceRepository;

    @RequestMapping(value = "/patterns/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Pattern> getPattern(@PathVariable String id) {
        Optional<Pattern> pattern = patternInterfaceRepository.findById(id);
        if (pattern.isPresent())
            return new ResponseEntity<Pattern>(pattern.get(), HttpStatus.ACCEPTED);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/patterns/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Integer> getPatternCount() {
        return new ResponseEntity<Integer>((int) patternInterfaceRepository.count(), HttpStatus.ACCEPTED);
    }

}
