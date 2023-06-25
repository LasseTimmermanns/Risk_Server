package de.lasse.risk_server.Lobby.Color;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColorInterfaceRepository extends MongoRepository<Color, String> {

    public List<Color> findAll();

}
