package de.lasse.risk_server.Game.Map;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapInterfaceRepository extends MongoRepository<Map, String> {

    public Map findMapById(String id);

    // @Query(value = "{ '_id' : ?0 }", fields = "{ 'territories' : { '$size' : 1 }
    // }")
    @Aggregation(pipeline = {
            "{$match: { _id : ?0 }}",
            "{$project: { size: { $size: \"$territories\" } }}"
    })
    public int findTerritoriesNum(String id);
}
