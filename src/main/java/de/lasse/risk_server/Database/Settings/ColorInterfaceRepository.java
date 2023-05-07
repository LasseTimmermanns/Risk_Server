package de.lasse.risk_server.Database.Settings;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColorInterfaceRepository extends MongoRepository<Color, String> {

    public List<Color> findAll();

}
