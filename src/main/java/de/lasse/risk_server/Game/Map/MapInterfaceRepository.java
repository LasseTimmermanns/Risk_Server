package de.lasse.risk_server.Game.Map;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapInterfaceRepository extends MongoRepository<Map, String> {

    public Map findMapById(String id);
}
