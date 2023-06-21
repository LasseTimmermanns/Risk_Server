package de.lasse.risk_server.Database.Maps;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DisplayMapInterfaceRepository extends MongoRepository<DisplayMap, String> {

    public DisplayMap findDisplayMapById(String id);

    public List<DisplayMap> findAll();
}
