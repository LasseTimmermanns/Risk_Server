package de.lasse.risk_server.Database.Settings;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.lasse.risk_server.Database.Lobby.Color;

public interface ColorInterfaceRepository extends MongoRepository<Color, String> {

    public List<Color> findAll();

}
