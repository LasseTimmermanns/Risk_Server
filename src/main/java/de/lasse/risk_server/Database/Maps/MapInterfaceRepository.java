package de.lasse.risk_server.Database.Maps;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapInterfaceRepository extends MongoRepository<Map, String> {

    public Map findMapByName(String name);

    public DisplayMap findDisplayMapByName(String name);
}
