package de.lasse.risk_server.Game.Game;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface GameInterfaceRepository extends MongoRepository<Game, String> {

    public Game findById();

    @Query(value = "{ '_id': ?0 }", fields = "{'players': 1}")
    public Optional<Game> findPlayersByGameId(String id);

    @Query(value = "{ '_id': ?0 }", fields = "{'_id': 1, 'players': 1, 'territories': 1, 'settings': 1, 'move': 1}")
    public Optional<Game> findStartingGameInformationById();

}
