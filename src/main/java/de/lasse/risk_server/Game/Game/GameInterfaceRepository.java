package de.lasse.risk_server.Game.Game;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameInterfaceRepository extends MongoRepository<Game, String> {

    public Game findById();

}
