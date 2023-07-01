package de.lasse.risk_server.Game.Map;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MapInterfaceRepository extends MongoRepository<Map, String> {

    @Query(value = "{ '_id' : ?0 }", fields = "{'_id' : 1, 'svg_width' : 1, 'svg_height': 1, 'territories': 1}")
    public Optional<Map> findById(String id);

    @Aggregation(pipeline = {
            "{$match: { _id : ?0 }}",
            "{$project: { size: { $size: \"$territories\" } }}"
    })
    public int findTerritoriesNum(String id);

    public List<Map> findAll();
}
