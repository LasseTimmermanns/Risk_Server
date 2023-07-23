package de.lasse.risk_server.Game.Shape;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class ShapeController {

    @Autowired
    ShapeInterfaceRepository shapeInterfaceRepository;

    @RequestMapping(value = "/shapes/getone/{pos}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Shape> getShape(@PathVariable int pos) {
        Optional<Shape> shape = shapeInterfaceRepository.findByPos(pos);
        if (shape.isPresent())
            return new ResponseEntity<Shape>(shape.get(), HttpStatus.ACCEPTED);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/shapes/getmany/{n}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Shape>> getNShapes(@PathVariable int n) {
        Pageable pageable = PageRequest.of(0, n);
        List<Shape> shapes = shapeInterfaceRepository.findLimited(pageable);
        return new ResponseEntity<List<Shape>>(shapes, HttpStatus.OK);
    }
}
